package com.example.ultramemorycore;

import com.example.ultramemorycore.memory.FastPropertyMap;
import com.example.ultramemorycore.memory.SharedPropertyMap;
import com.example.ultramemorycore.memory.UltraFastPropertyMap;
import com.example.ultramemorycore.pool.ArrayPools;
import com.example.ultramemorycore.pool.PaletteArrayPool;
import com.example.ultramemorycore.pool.UploadBufferPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UltraMemoryCore {

    public static final String MOD_ID = "ultramemorycore";

    // Logger để các class Common dùng được
    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

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

        // Chỉ gọi các class còn nằm trong Common
        UltraFastPropertyMap.clear();
        FastPropertyMap.clear();
        SharedPropertyMap.clear();

        PaletteArrayPool.clear();
        ArrayPools.clearAll();
        UploadBufferPool.clear();

        LOGGER.info("[UltraMemoryCore] Common memory pools trimmed.");
    }
}
