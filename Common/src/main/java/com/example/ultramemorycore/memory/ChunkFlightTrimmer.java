package com.example.ultramemorycore.memory;

import com.example.ultramemorycore.cache.BlockStatePaletteCache;
import com.example.ultramemorycore.cache.NbtStringPool;

public final class ChunkFlightTrimmer {

    private static long lastTrim = 0L;

    private ChunkFlightTrimmer() {}

    public static void tick() {

        long now = System.currentTimeMillis();

        // 5 giây mới trim một lần để tránh tốn CPU
        if (now - lastTrim < 5000L) {
            return;
        }

        lastTrim = now;

        // Chỉ trim cache chunk-related
        BlockStatePaletteCache.clear();
        UltraFastPropertyMap.trim();
        NbtStringPool.trim();
    }
}
