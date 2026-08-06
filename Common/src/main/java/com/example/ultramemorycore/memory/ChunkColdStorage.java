package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkColdStorage {

    private static final Map<Long, Long> LAST_VISIBLE =
            new ConcurrentHashMap<>();

    // Giới hạn sweep tối đa 1 lần mỗi 10 giây
    private static long lastSweep = 0L;

    private ChunkColdStorage() {}

    // ===== Adaptive timings =====

    private static long softCleanupMs() {

        if (MemoryBudget.isLowEnd()) {
            return 5_000L;
        }

        if (MemoryBudget.isMidRange()) {
            return 15_000L;
        }

        return 30_000L;
    }

    private static long hardEvictMs() {

        if (MemoryBudget.isLowEnd()) {
            return 60_000L;
        }

        if (MemoryBudget.isMidRange()) {
            return 180_000L;
        }

        return 300_000L;
    }

    // ===== Tracking =====

    public static void markVisible(ChunkPos pos) {

        LAST_VISIBLE.put(
                pos.toLong(),
                System.currentTimeMillis()
        );
    }

    // ===== Main tick =====

    public static void tick(MinecraftClient client) {

        if (client.world == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();

        // Emergency cleanup khi RAM quá cao
        if (MemoryPressure.isCritical()) {

            VoxelShapeCache.clear();

            UltraFastPropertyMap.clear();

            System.gc();

        } else if (MemoryPressure.isHigh()) {

            VoxelShapeCache.sweep();

            UltraFastPropertyMap.trim();
        }

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

            // Giữ thêm 4 chunk đệm ngoài view distance
            int limit =
                    client.options.getViewDistance().getValue() + 4;

            // Chunk đã ra khỏi vùng nhìn một thời gian
            if (age > softCleanupMs()
                    && (dx > limit || dz > limit)) {

                needSweep = true;
            }

            // Quá lâu thì bỏ khỏi tracking
            if (age > hardEvictMs()) {
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

    // ===== Debug =====

    public static int size() {
        return LAST_VISIBLE.size();
    }

    public static void clear() {
        LAST_VISIBLE.clear();
    }
}
