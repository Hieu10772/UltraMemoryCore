package com.example.ultramemorycore.memory;

import com.example.ultramemorycore.UltraMemoryCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public final class IdleMemoryBalancer {

    private static double lastX;
    private static double lastY;
    private static double lastZ;

    private static long idleSince = 0L;
    private static long lastCleanup = 0L;

    private IdleMemoryBalancer() {}

    public static void tick(Minecraft client) {

        EntityPlayerSP player = client.player;

        if (player == null) {
            return;
        }

        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        boolean moved =
                Math.abs(x - lastX) > 0.2 ||
                Math.abs(y - lastY) > 0.2 ||
                Math.abs(z - lastZ) > 0.2;

        lastX = x;
        lastY = y;
        lastZ = z;

        if (moved) {
            idleSince = 0L;
            return;
        }

        if (idleSince == 0L) {
            idleSince = System.currentTimeMillis();
            return;
        }

        long now = System.currentTimeMillis();

        if (now - idleSince > 20000L &&
                now - lastCleanup > 60000L) {

            Runtime r = Runtime.getRuntime();
            long usedMB = (r.totalMemory() - r.freeMemory()) / (1024 * 1024);

            if (usedMB > 1200) {

                UltraMemoryCore.trimAllCaches();

                System.gc();

                lastCleanup = now;

                UltraMemoryCore.LOGGER.info(
                        "[UltraMemoryCore] Idle memory balancing triggered at {} MB",
                        usedMB
                );
            }
        }
    }
}
