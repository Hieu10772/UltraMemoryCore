package com.example.ultramemorycore.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FastPropertyMap {

    private static final ConcurrentHashMap<Map<?, ?>, Map<?, ?>> INTERN =
            new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> intern(Map<K, V> original) {
        if (original == null || original.isEmpty()) {
            return original;
        }

        Map<?, ?> existing = INTERN.get(original);
        if (existing != null) {
            return (Map<K, V>) existing;
        }

        existing = INTERN.putIfAbsent(original, original);
        return (Map<K, V>) (existing != null ? existing : original);
    }

    public static int size() {
        return INTERN.size();
    }

    public static void clear() {
        INTERN.clear();
    }
}
