package com.example.ultramemorycore.client;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.memory.AdaptiveCacheManager;
import com.example.ultramemorycore.memory.WeakCacheSweeper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class UltraMemoryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!UltraMemoryCore.isEnabled()) {
                return;
            }
            if (client.level == null || client.player == null) {
                return;
            }

            AdaptiveCacheManager.tick();
            WeakCacheSweeper.tick();
        });

    }
}
