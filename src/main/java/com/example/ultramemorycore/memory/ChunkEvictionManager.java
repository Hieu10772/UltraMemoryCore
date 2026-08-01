package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkEvictionManager {

    private static final Map<Long, Long> LAST_SEEN =
            new ConcurrentHashMap<>();

    private static final long SOFT_CLEANUP_MS = 5_000L;
    private static final long HARD_UNLOAD_MS = 120_000L;

    private ChunkEvictionManager() {}

    public static void markVisible(ChunkPos pos) {
        LAST_SEEN.put(pos.toLong(), System.currentTimeMillis());
    }

    public static void tick(MinecraftClient client) {

        ClientWorld world = client.world;

        if (world == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();

        Iterator<Map.Entry<Long, Long>> it = LAST_SEEN.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<Long, Long> entry = it.next();

            long age = now - entry.getValue();

            // 5 giây: dọn cache nhẹ
            if (age > SOFT_CLEANUP_MS) {
                VoxelShapeCache.sweep();
            }

            // 2 phút: unload hẳn chunk khỏi client
            if (age > HARD_UNLOAD_MS) {

                ChunkPos pos = new ChunkPos(entry.getKey());

                // Chỉ unload chunk ở rất xa người chơi
                int dx = Math.abs(pos.x - client.player.getChunkPos().x);
                int dz = Math.abs(pos.z - client.player.getChunkPos().z);

                if (dx > 12 || dz > 12) {

                    world.unloadBlockEntities(world.getChunk(pos.x, pos.z));

                    it.remove();
                }
            }
        }
    }

    public static void clear() {
        LAST_SEEN.clear();
    }
}
