package com.example.ultramemorycore.memory;

import net.minecraft.block.properties.IProperty;

import java.util.Map;

public final class SharedPropertyMap {

    private SharedPropertyMap() {}

    public static Map<IProperty<?>, Comparable<?>> share(
            Map<IProperty<?>, Comparable<?>> map
    ) {
        return UltraFastPropertyMap.intern(map);
    }

    public static int size() {
        return UltraFastPropertyMap.size();
    }
    
    public static void trim() {
        UltraFastPropertyMap.trim();
    }

    public static void clear() {
        UltraFastPropertyMap.clear();
    }
}
