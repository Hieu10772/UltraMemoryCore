package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;

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
                ChunkPos.asLong(pos.x(), pos.z()),
                System.currentTimeMillis()
        );
    }

    public static void tick(Minecraft client) {
        if (client.level == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();
        ChunkPos playerPos = client.player.chunkPosition();
        boolean needSweep = false;

        Iterator<Map.Entry<Long, Long>> it = LAST_SEEN.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Long, Long> entry = it.next();
            long age = now - entry.getValue();

            long chunkKey = entry.getKey();
            ChunkPos pos = new ChunkPos(ChunkPos.getX(chunkKey), ChunkPos.getZ(chunkKey));

            int dx = Math.abs(pos.x() - playerPos.x());
            int dz = Math.abs(pos.z() - playerPos.z());

            int limit = client.options.renderDistance().get() + 4;

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
