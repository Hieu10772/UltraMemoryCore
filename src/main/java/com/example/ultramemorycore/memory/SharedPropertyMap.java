package com.example.ultramemorycore.memory;

import net.minecraft.world.level.block.state.properties.Property;

import java.util.Map;

public final class SharedPropertyMap {

    private SharedPropertyMap() {}

    public static Map<Property<?>, Comparable<?>> share(
            Map<Property<?>, Comparable<?>> map
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
