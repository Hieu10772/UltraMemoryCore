package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkEvictionManager {

    private static final Map<Long, Long> LAST_SEEN =
            new ConcurrentHashMap<>();

    // Sau 5 giây mới bắt đầu coi là chunk lạnh
    private static final long SOFT_CLEANUP_MS = 5_000L;

    // Sau 2 phút thì bỏ khỏi tracking
    private static final long HARD_UNLOAD_MS = 120_000L;

    // Chống sweep liên tục
    private static long lastSweep = 0L;

    private ChunkEvictionManager() {}

    public static void markVisible(ChunkPos pos) {
        LAST_SEEN.put(
                pos.toLong(),
                System.currentTimeMillis()
        );
    }

    public static void tick(MinecraftClient client) {

        if (client.world == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();

        ChunkPos playerPos = client.player.getChunkPos();

        boolean needSweep = false;

        Iterator<Map.Entry<Long, Long>> it =
                LAST_SEEN.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<Long, Long> entry = it.next();

            long age = now - entry.getValue();

            ChunkPos pos = new ChunkPos(entry.getKey());

            int dx = Math.abs(pos.x - playerPos.x);
            int dz = Math.abs(pos.z - playerPos.z);

            int limit =
                    client.options.getViewDistance().getValue() + 4;

            // Chunk đã ở xa một thời gian
            if (age > SOFT_CLEANUP_MS &&
                    (dx > limit || dz > limit)) {

                needSweep = true;
            }

            // Quá 2 phút thì bỏ khỏi tracking
            if (age > HARD_UNLOAD_MS) {
                it.remove();
            }
        }

        // Chỉ dọn cache tối đa 1 lần mỗi 10 giây
        if (needSweep && now - lastSweep > 10_000L) {

            VoxelShapeCache.sweep();

            UltraFastPropertyMap.trim();

            lastSweep = now;
        }
    }

    public static int size() {
        return LAST_SEEN.size();
    }

    public static void clear() {
        LAST_SEEN.clear();
    }
}
