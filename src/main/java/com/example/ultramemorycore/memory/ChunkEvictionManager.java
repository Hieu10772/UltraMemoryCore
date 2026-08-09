package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkEvictionManager {

    private static final Map<Long, Long> LAST_SEEN = new ConcurrentHashMap<>();

    private static final long SOFT_CLEANUP_MS = 5_000L;
    private static final long HARD_UNLOAD_MS = 120_000L;

    private static long lastSweep = 0L;

    private ChunkEvictionManager() {}

    public static void markVisible(ChunkPos pos) {
        LAST_SEEN.put(
                ChunkPos.asLong(pos.x, pos.z),
                System.currentTimeMillis()
        );
    }

    public static void tick(Minecraft client) {
        if (client.world == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();

        int playerChunkX = client.player.chunkCoordX;
        int playerChunkZ = client.player.chunkCoordZ;

        boolean needSweep = false;

        Iterator<Map.Entry<Long, Long>> it = LAST_SEEN.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Long, Long> entry = it.next();

            long age = now - entry.getValue();
            long chunkKey = entry.getKey();

            int chunkX = (int) chunkKey;
            int chunkZ = (int) (chunkKey >>> 32);

            int dx = Math.abs(chunkX - playerChunkX);
            int dz = Math.abs(chunkZ - playerChunkZ);

            int limit = client.gameSettings.renderDistanceChunks + 4;

            if (age > SOFT_CLEANUP_MS && (dx > limit || dz > limit)) {
                needSweep = true;
            }

            if (age > HARD_UNLOAD_MS) {
                it.remove();
            }
        }

        if (needSweep && now - lastSweep > 10_000L) {
            VoxelShapeCache.sweep();
            UltraFastPropertyMap.trim();
            lastSweep = now;
        }
    }

    public static int size() {
        return LAST_SEEN.size();
    }

    public static void clear() {
        LAST_SEEN.clear();
    }
}
