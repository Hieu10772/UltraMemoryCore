package com.example.ultramemorycore.client;

import com.example.ultramemorycore.memory.AdaptiveCacheManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class UltraMemoryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            AdaptiveCacheManager.tick();

        });

    }
}
