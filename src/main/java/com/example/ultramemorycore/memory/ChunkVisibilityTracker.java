package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;

public final class ChunkVisibilityTracker {

    private ChunkVisibilityTracker() {}

    public static void tick(Minecraft client) {

        if (client.player == null) {
            return;
        }

        int centerX = client.player.chunkCoordX;
        int centerZ = client.player.chunkCoordZ;

        int viewDistance = client.gameSettings.renderDistanceChunks;

        int keepRadius = Math.max(6, viewDistance + 2);

        for (int x = -keepRadius; x <= keepRadius; x++) {
            for (int z = -keepRadius; z <= keepRadius; z++) {

                ChunkPos pos = new ChunkPos(
                        centerX + x,
                        centerZ + z
                );

                ChunkEvictionManager.markVisible(pos);

                ChunkColdStorage.markVisible(pos);
            }
        }
    }
}
