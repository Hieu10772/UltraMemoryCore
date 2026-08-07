package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class FlightModeDetector {

    private FlightModeDetector() {}

    public static boolean isFastElytraFlight() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) return false;

        ClientPlayerEntity player = client.player;

        if (player == null) return false;

        if (!player.isGliding()) return false;

        double speed = player.getVelocity().length();

        return speed > 1.2; // khoảng 20+ m/s
    }
}
