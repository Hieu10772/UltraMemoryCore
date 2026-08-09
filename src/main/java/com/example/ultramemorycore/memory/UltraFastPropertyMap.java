package com.example.ultramemorycore.memory;

import net.minecraft.block.properties.IProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UltraFastPropertyMap {

    private static final ConcurrentHashMap<PropertyKey, Map<IProperty<?>, Comparable<?>>> INTERN =
            new ConcurrentHashMap<>();

    private static final int MAX_ENTRIES = 4096;

    private UltraFastPropertyMap() {}

    public static Map<IProperty<?>, Comparable<?>> intern(
            Map<IProperty<?>, Comparable<?>> original
    ) {

        if (original == null || original.isEmpty()) {
            return original;
        }

        if (INTERN.size() > MAX_ENTRIES) {
            INTERN.clear();
        }

        Map<IProperty<?>, Comparable<?>> immutable = Collections.unmodifiableMap(new HashMap<>(original));

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

    private static final class PropertyKey {
        private final int hash;
        private final int size;

        public PropertyKey(int hash, int size) {
            this.hash = hash;
            this.size = size;
        }

        static PropertyKey from(Map<IProperty<?>, Comparable<?>> map) {

            int hash = 1;

            for (Map.Entry<IProperty<?>, Comparable<?>> entry : map.entrySet()) {

                hash = 31 * hash + System.identityHashCode(entry.getKey());

                hash = 31 * hash + entry.getValue().hashCode();
            }

            return new PropertyKey(hash, map.size());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PropertyKey)) return false;
            PropertyKey that = (PropertyKey) o;
            return hash == that.hash && size == that.size;
        }

        @Override
        public int hashCode() {
            return 31 * hash + size;
        }
    }
}
