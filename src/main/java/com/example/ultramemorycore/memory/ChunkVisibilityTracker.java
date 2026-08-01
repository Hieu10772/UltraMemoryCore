package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

public final class ChunkVisibilityTracker {

    private ChunkVisibilityTracker() {}

    public static void tick(MinecraftClient client) {

        if (client.player == null) {
            return;
        }

        ChunkPos center = client.player.getChunkPos();

        int view = client.options.getViewDistance().getValue();

        for (int x = -view; x <= view; x++) {
            for (int z = -view; z <= view; z++) {
                ChunkEvictionManager.markVisible(
                        new ChunkPos(center.x + x, center.z + z)
                );
            }
        }
    }
}
