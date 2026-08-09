package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public final class FlightModeDetector {

    private FlightModeDetector() {}

    public static boolean isFastElytraFlight() {
        Minecraft client = Minecraft.getMinecraft();

        if (client == null) return false;

        EntityPlayerSP player = client.player;

        if (player == null) return false;

        if (!player.isElytraFlying()) return false;

        double speed = Math.sqrt(
                player.motionX * player.motionX +
                player.motionY * player.motionY +
                player.motionZ * player.motionZ
        );

        return speed > 1.2;
    }
}
