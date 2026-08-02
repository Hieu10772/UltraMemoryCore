package com.example.ultramemorycore.memory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;

public final class ChunkVisibilityTracker {

    private ChunkVisibilityTracker() {}

    public static void tick(MinecraftClient client) {

        if (client.player == null || client.world == null) {
            return;
        }

        ClientWorld world = client.world;

        ChunkPos center = client.player.getChunkPos();

        int viewDistance = client.options.getViewDistance().getValue();

        // Giữ thêm 2 chunk đệm ngoài view distance
        int keepRadius = Math.max(6, viewDistance + 2);

        int playerChunkX = center.x;
        int playerChunkZ = center.z;

        // Duyệt toàn bộ chunk đang được client giữ
        for (ChunkPos pos : world.getChunkManager().chunks.keySet()) {

            int dx = Math.abs(pos.x - playerChunkX);
            int dz = Math.abs(pos.z - playerChunkZ);

            if (dx <= keepRadius && dz <= keepRadius) {

                // Chunk còn trong vùng nhìn
                ChunkColdStorage.markVisible(pos);

            } else {

                // Chunk đã ở xa → bắt đầu đếm thời gian lạnh
                ChunkColdStorage.markInvisible(pos);
            }
        }
    }
}
