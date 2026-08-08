package com.example.ultramemorycore.memory;

import com.example.ultramemorycore.UltraMemoryCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class PostFlightCleanup {

    private static boolean wasFlying = false;
    private static long landedAt = 0L;

    private PostFlightCleanup() {}

    public static void tick(Minecraft client) {

        LocalPlayer player = client.player;

        if (player == null) {
            return;
        }

        boolean flying = player.isFallFlying();

        // Vừa bắt đầu bay
        if (flying) {
            wasFlying = true;
            landedAt = 0L;
            return;
        }

        // Vừa kết thúc bay
        if (wasFlying && landedAt == 0L) {
            landedAt = System.currentTimeMillis();
            return;
        }

        // Sau khi đáp 4 giây mới cleanup
        if (landedAt != 0L &&
                System.currentTimeMillis() - landedAt > 4000L) {

            UltraMemoryCore.trimAllCaches();

            System.gc();

            UltraMemoryCore.LOGGER.info(
                    "[UltraMemoryCore] Post-flight cleanup executed."
            );

            wasFlying = false;
            landedAt = 0L;
        }
    }
}
