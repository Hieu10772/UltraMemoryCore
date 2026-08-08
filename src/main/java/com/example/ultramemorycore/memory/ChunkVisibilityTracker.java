package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;

public final class ChunkVisibilityTracker {

    private ChunkVisibilityTracker() {}

    public static void tick(Minecraft client) {

        if (client.player == null) {
            return;
        }

        ChunkPos center = client.player.chunkPosition();

        int viewDistance =
                client.options.renderDistance().get();

        // Giữ thêm 2 chunk đệm ngoài view distance
        int keepRadius = Math.max(6, viewDistance + 2);

        for (int x = -keepRadius; x <= keepRadius; x++) {
            for (int z = -keepRadius; z <= keepRadius; z++) {

                ChunkPos pos = new ChunkPos(
                        center.x + x,
                        center.z + z
                );

                // Chunk đang còn trong vùng nhìn
                ChunkEvictionManager.markVisible(pos);

                ChunkColdStorage.markVisible(pos);
            }
        }
    }
}
