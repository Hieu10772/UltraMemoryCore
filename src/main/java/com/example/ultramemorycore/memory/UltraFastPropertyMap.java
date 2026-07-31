package com.example.ultramemorycore.memory;

import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UltraFastPropertyMap {

    private static final ConcurrentHashMap<PropertyKey, Map<Property<?>, Comparable<?>>> INTERN =
            new ConcurrentHashMap<>();

    private UltraFastPropertyMap() {}

    public static Map<Property<?>, Comparable<?>> intern(
            Map<Property<?>, Comparable<?>> original
    ) {
        if (original == null || original.isEmpty()) {
            return original;
        }

        PropertyKey key = PropertyKey.from(original);

        return INTERN.computeIfAbsent(key, k -> original);
    }

    public static int size() {
        return INTERN.size();
    }

    public static void clear() {
        INTERN.clear();
    }

    private record PropertyKey(int hash, int size) {

        static PropertyKey from(Map<Property<?>, Comparable<?>> map) {
            int hash = 1;

            Collection<Map.Entry<Property<?>, Comparable<?>>> entries = map.entrySet();

            for (Map.Entry<Property<?>, Comparable<?>> entry : entries) {
                hash = 31 * hash + entry.getKey().getName().hashCode();
                hash = 31 * hash + entry.getValue().hashCode();
            }

            return new PropertyKey(hash, map.size());
        }
    }
}
