package com.example.ultramemorycore.memory;

import com.example.ultramemorycore.UltraMemoryCoreFabric;
import com.example.ultramemorycore.UltraMemoryCore;

public final class AdaptiveCacheManager {

    private static long lastTrimTime = 0;

    private AdaptiveCacheManager() {}

    public static void tick() {

        long now = System.currentTimeMillis();

        // chỉ kiểm tra mỗi 5 giây
        if (now - lastTrimTime < 5000) {
            return;
        }

        lastTrimTime = now;

        Runtime runtime = Runtime.getRuntime();

        long used = runtime.totalMemory() - runtime.freeMemory();

        long max = runtime.maxMemory();

        double usage = (double) used / max;

        if (usage > 0.90) {

            UltraMemoryCore.trimAllCaches();

            System.gc();

            UltraMemoryCoreFabric.LOGGER.info(
                    "[UMC] Emergency memory cleanup ({}%)",
                    (int)(usage * 100)
            );

        } else if (usage > 0.75) {
            VoxelShapeCache.sweep();

            UltraMemoryCore.trimAllCaches();

            UltraMemoryCoreFabric.LOGGER.info(
                    "[UMC] Soft memory cleanup ({}%)",
                    (int)(usage * 100)
            );
        }
    }
}
