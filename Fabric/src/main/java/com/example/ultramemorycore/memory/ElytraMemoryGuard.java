package com.example.ultramemorycore.memory;

import com.example.ultramemorycore.UltraMemoryCore;

public final class ElytraMemoryGuard {

    // iPad RAM thấp: khoảng 1.25GB bắt đầu trim mạnh
    private static final long SOFT_LIMIT_MB = 1250;

    // 1.35GB thì ép dọn mạnh + GC
    private static final long HARD_LIMIT_MB = 1350;

    private static long lastHardCleanup = 0L;

    private ElytraMemoryGuard() {}

    public static void tick() {

        Runtime r = Runtime.getRuntime();

        long usedMB = (r.totalMemory() - r.freeMemory()) / (1024 * 1024);

        // Trim nhẹ
        if (usedMB > SOFT_LIMIT_MB) {
            ChunkFlightTrimmer.tick();
        }

        // Trim mạnh + GC có cooldown
        if (usedMB > HARD_LIMIT_MB) {

            long now = System.currentTimeMillis();

            if (now - lastHardCleanup > 10000L) {

                UltraMemoryCore.trimAllCaches();

                System.gc();

                lastHardCleanup = now;

                UltraMemoryCore.LOGGER.info(
                        "[UltraMemoryCore] Elytra emergency cleanup triggered at {} MB",
                        usedMB
                );
            }
        }
    }
}
