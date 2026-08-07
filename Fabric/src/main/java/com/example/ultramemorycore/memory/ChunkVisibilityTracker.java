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

        int viewDistance =
                client.options.getViewDistance().getValue();

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
