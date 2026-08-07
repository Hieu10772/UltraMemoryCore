package com.example.ultramemorycore.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FastPropertyMap {

    private static final ConcurrentHashMap<Integer, Map<?, ?>> INTERN =
            new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> intern(Map<K, V> original) {
        if (original == null || original.isEmpty()) {
            return original;
        }

        int hash = original.hashCode();
        return (Map<K, V>) INTERN.computeIfAbsent(hash, k -> original);
    }

    public static int size() {
        return INTERN.size();
    }

    public static void clear() {
        INTERN.clear();
    }
}
