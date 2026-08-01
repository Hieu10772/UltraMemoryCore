package com.example.ultramemorycore.memory;

import net.minecraft.state.property.Property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UltraFastPropertyMap {

    private static final ConcurrentHashMap<PropertyKey, Map<Property<?>, Comparable<?>>> INTERN =
            new ConcurrentHashMap<>();

    private static final int MAX_ENTRIES = 4096;

    private UltraFastPropertyMap() {}

    public static Map<Property<?>, Comparable<?>> intern(
            Map<Property<?>, Comparable<?>> original
    ) {

        if (original == null || original.isEmpty()) {
            return original;
        }

        if (INTERN.size() > MAX_ENTRIES) {
            INTERN.clear();
        }

        Map<Property<?>, Comparable<?>> immutable = Map.copyOf(original);

        PropertyKey key = PropertyKey.from(immutable);

        return INTERN.computeIfAbsent(key, k -> immutable);
    }

    public static int size() {
        return INTERN.size();
    }

    public static void clear() {
        INTERN.clear();
    }

    public static void trim() {
        if (INTERN.size() > 2048) {
            INTERN.clear();
        }
    }

    private record PropertyKey(int hash, int size) {

        static PropertyKey from(
                Map<Property<?>, Comparable<?>> map
        ) {

            int hash = 1;

            for (Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {

                hash = 31 * hash +
                        System.identityHashCode(entry.getKey());

                hash = 31 * hash +
                        entry.getValue().hashCode();
            }

            return new PropertyKey(hash, map.size());
        }
    }
}
