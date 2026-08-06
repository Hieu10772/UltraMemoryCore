package com.example.ultramemorycore.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryRefCache {
    private static final Map<String, Object> REFS = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getOrCreate(String key, java.util.function.Supplier<T> supplier) {
        return (T) REFS.computeIfAbsent(key, k -> supplier.get());
    }

    public static void clear() {
        REFS.clear();
    }
}
