package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkColdStorage {

    private static final Map<Long, Long> LAST_VISIBLE =
            new ConcurrentHashMap<>();

    // 5 giây: dọn cache nhẹ
    private static final long SOFT_CLEANUP_MS = 5_000L;

    // 60 giây: unload chunk xa
    private static final long HARD_EVICT_MS = 60_000L;

    private ChunkColdStorage() {}

    public static void markVisible(ChunkPos pos) {
        LAST_VISIBLE.put(pos.toLong(), System.currentTimeMillis());
    }

    public static void tick(MinecraftClient client) {

        if (client.world == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();

        ClientChunkManager chunkManager = client.world.getChunkManager();
        ChunkPos playerPos = client.player.getChunkPos();

        Iterator<Map.Entry<Long, Long>> it = LAST_VISIBLE.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<Long, Long> entry = it.next();

            long age = now - entry.getValue();

            // Dọn cache nhẹ sau 5 giây
            if (age > SOFT_CLEANUP_MS) {
                VoxelShapeCache.sweep();
                UltraFastPropertyMap.trim();
            }

            // Unload chunk ở xa sau 60 giây
            if (age > HARD_EVICT_MS) {

                ChunkPos pos = new ChunkPos(entry.getKey());

                int dx = Math.abs(pos.x - playerPos.x);
                int dz = Math.abs(pos.z - playerPos.z);

                int limit = client.options.getViewDistance().getValue() + 4;

                if (dx > limit || dz > limit) {

                    chunkManager.unload(pos);

                    VoxelShapeCache.sweep();
                    UltraFastPropertyMap.trim();

                    it.remove();
                }
            }
        }
    }

    public static void clear() {
        LAST_VISIBLE.clear();
    }
}
