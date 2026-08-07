package com.example.ultramemorycore.memory;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.pool.UploadBufferPool;

public final class ChunkGovernor {

    private static long lastTrim = 0L;

    private static boolean aggressive = false;

    private ChunkGovernor() {}

    public static void tick() {

        Runtime runtime = Runtime.getRuntime();

        long usedMB =
                (runtime.totalMemory() - runtime.freeMemory())
                        / (1024 * 1024);

        boolean flight = FlightModeDetector.isFastElytraFlight();

        long now = System.currentTimeMillis();

        // Chế độ Elytra tốc độ cao
        if (flight && usedMB > 1100) {

            if (!aggressive) {

                aggressive = true;

                UploadBufferPool.configure(4L * 1024L * 1024L);

                UltraMemoryCore.LOGGER.info(
                        "[UMC Governor] Entered Flight Mode"
                );
            }

            if (now - lastTrim > 5000) {

                UltraMemoryCore.trimAllCaches();

                lastTrim = now;
            }

            return;
        }

        // Khôi phục chế độ bình thường
        if (aggressive) {

            aggressive = false;

            UploadBufferPool.configure(16L * 1024L * 1024L);

            UltraMemoryCore.LOGGER.info(
                    "[UMC Governor] Restored Normal Mode"
            );
        }

        // Bảo vệ khi RAM quá cao
        if (usedMB > 1350 && now - lastTrim > 10000) {

            UltraMemoryCore.trimAllCaches();

            System.gc();

            lastTrim = now;

            UltraMemoryCore.LOGGER.warn(
                    "[UMC Governor] Emergency trim at {} MB",
                    usedMB
            );
        }
    }
}
