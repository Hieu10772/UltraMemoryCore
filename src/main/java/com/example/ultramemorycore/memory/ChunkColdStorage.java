package com.example.ultramemorycore.memory;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkColdStorage {

    private static final Map<Long, Long> LAST_VISIBLE = new ConcurrentHashMap<>();
    private static long lastSweep = 0L;

    private ChunkColdStorage() {}

    private static long softCleanupMs() {
        if (MemoryBudget.isLowEnd()) return 5_000L;
        if (MemoryBudget.isMidRange()) return 15_000L;
        return 30_000L;
    }

    private static long hardEvictMs() {
        if (MemoryBudget.isLowEnd()) return 60_000L;
        if (MemoryBudget.isMidRange()) return 180_000L;
        return 300_000L;
    }

    public static void markVisible(ChunkPos pos) {
        LAST_VISIBLE.put(
                ChunkPos.asLong(pos.x(), pos.z()),
                System.currentTimeMillis()
        );
    }

    public static void tick(Minecraft client) {
        if (client.level == null || client.player == null) {
            return;
        }

        long now = System.currentTimeMillis();

        if (MemoryPressure.isCritical()) {
            VoxelShapeCache.clear();
            UltraFastPropertyMap.clear();
            System.gc();
        } else if (MemoryPressure.isHigh()) {
            VoxelShapeCache.sweep();
            UltraFastPropertyMap.trim();
        }

        ChunkPos playerPos = client.player.chunkPosition();
        boolean needSweep = false;

        Iterator<Map.Entry<Long, Long>> it = LAST_VISIBLE.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Long, Long> entry = it.next();
            long age = now - entry.getValue();

            long chunkKey = entry.getKey();
            ChunkPos pos = new ChunkPos(ChunkPos.getX(chunkKey), ChunkPos.getZ(chunkKey));

            int dx = Math.abs(pos.x() - playerPos.x());
            int dz = Math.abs(pos.z() - playerPos.z());

            int limit = client.options.renderDistance().get() + 4;

            if (age > softCleanupMs() && (dx > limit || dz > limit)) {
                needSweep = true;
            }

            if (age > hardEvictMs()) {
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
        return LAST_VISIBLE.size();
    }

    public static void clear() {
        LAST_VISIBLE.clear();
    }
}
