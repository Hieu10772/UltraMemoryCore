package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkColdStorage {

    private static final Map<Long, Long> LAST_VISIBLE =
            new ConcurrentHashMap<>();

    // Sau 5 giây mới bắt đầu coi là chunk lạnh
    private static final long SOFT_CLEANUP_MS = 5_000L;

    // Sau 60 giây thì bỏ khỏi tracking
    private static final long HARD_EVICT_MS = 60_000L;

    // Giới hạn sweep mỗi 10 giây
    private static long lastSweep = 0L;

    private ChunkColdStorage() {}

    public static void markVisible(ChunkPos pos) {
        LAST_VISIBLE.put(
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
                LAST_VISIBLE.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<Long, Long> entry = it.next();

            long age = now - entry.getValue();

            ChunkPos pos = new ChunkPos(entry.getKey());

            int dx = Math.abs(pos.x - playerPos.x);
            int dz = Math.abs(pos.z - playerPos.z);

            int limit =
                    client.options.getViewDistance().getValue() + 4;

            // Chunk đã ra khỏi vùng nhìn một thời gian
            if (age > SOFT_CLEANUP_MS &&
                    (dx > limit || dz > limit)) {

                needSweep = true;
            }

            // Quá lâu thì bỏ khỏi tracking
            if (age > HARD_EVICT_MS) {
                it.remove();
            }
        }

        // Chỉ sweep tối đa 1 lần mỗi 10 giây
        if (needSweep && now - lastSweep > 10_000L) {

            VoxelShapeCache.sweep();

            UltraFastPropertyMap.trim();

            lastSweep = now;
        }
    }

    public static int size() {
        return LAST_VISIBLE.size();
    }

    public static void clear() {
        LAST_VISIBLE.clear();
    }
}
