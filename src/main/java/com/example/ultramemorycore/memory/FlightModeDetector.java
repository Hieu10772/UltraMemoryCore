package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class FlightModeDetector {

    private FlightModeDetector() {}

    public static boolean isFastElytraFlight() {
        Minecraft client = Minecraft.getInstance();

        if (client == null) return false;

        LocalPlayer player = client.player;

        if (player == null) return false;

        if (!player.isFallFlying()) return false;

        double speed = player.getDeltaMovement().length();

        return speed > 1.2; // khoảng 20+ m/s
    }
}
