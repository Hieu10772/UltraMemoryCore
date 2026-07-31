package com.example.ultramemorycore.memory;

import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UltraFastPropertyMap {

    private static final ConcurrentHashMap<PropertyKey, Map<Property<?>, Comparable<?>>> INTERN =
            new ConcurrentHashMap<>();
    private static final int MAX_ENTRIES = 8192;

    private UltraFastPropertyMap() {}

    public static Map<Property<?>, Comparable<?>> intern(
            Map<Property<?>, Comparable<?>> original
        if (INTERN.size() > MAX_ENTRIES) {

    INTERN.clear();

}
    ) {
        if (original == null || original.isEmpty()) {
            return original;
        }

        PropertyKey key = PropertyKey.from(original);

        return INTERN.computeIfAbsent(
        key,
        k -> Map.copyOf(original)
);
    }

    public static int size() {
        return INTERN.size();
    }

    public static void clear() {
        INTERN.clear();
    }

public static void trim() {

    if (INTERN.size() > MAX_ENTRIES / 2) {
        INTERN.clear();
    }
}

    private record PropertyKey(
        int hash,
        int size,
        Map<Property<?>, Comparable<?>> original
) {

    static PropertyKey from(Map<Property<?>, Comparable<?>> map) {

        int hash = 1;

        for (Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {

            hash = 31 * hash + System.identityHashCode(entry.getKey());

            hash = 31 * hash + entry.getValue().hashCode();

        }

        return new PropertyKey(
                hash,
                map.size(),
                map
        );
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PropertyKey other)) {
            return false;
        }

        if (hash != other.hash || size != other.size) {
            return false;
        }

        return original.equals(other.original);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
}
