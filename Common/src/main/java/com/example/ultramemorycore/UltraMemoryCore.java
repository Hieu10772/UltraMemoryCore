package com.example.ultramemorycore;

import com.example.ultramemorycore.cache.NbtStringPool;
import com.example.ultramemorycore.pool.ArrayPools;
import com.example.ultramemorycore.pool.PaletteArrayPool;
import com.example.ultramemorycore.pool.UploadBufferPool;

public final class UltraMemoryCore {

    public static final String MOD_ID = "ultramemorycore";

    private static volatile boolean enabled = true;

    private UltraMemoryCore() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static void bootstrap() {
        enabled = true;
    }

    public static void trimAllCaches() {

        // chỉ cleanup các pool/cache còn nằm trong Common
        NbtStringPool.trim();

        PaletteArrayPool.clear();
        ArrayPools.clearAll();
        UploadBufferPool.clear();
    }
}
