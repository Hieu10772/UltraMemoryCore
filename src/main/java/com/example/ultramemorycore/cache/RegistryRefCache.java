package com.example.ultramemorycore.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RegistryRefCache {
    private static final Map<String, Object> REFS = new ConcurrentHashMap<>();
    
    // Đánh dấu cho trường hợp supplier trả về null (tránh chạy lại supplier nhiều lần)
    private static final Object NULL_SENTINEL = new Object();

    @SuppressWarnings("unchecked")
    public static <T> T getOrCreate(String key, Supplier<T> supplier) {
        if (key == null) return null;

        // 1. Kiểm tra nhanh không dùng lock (Double-checked locking pattern an toàn)
        Object existing = REFS.get(key);
        if (existing != null) {
            return existing == NULL_SENTINEL ? null : (T) existing;
        }

        // 2. Tránh lỗi Deadlock của computeIfAbsent trên Java 8
        synchronized (RegistryRefCache.class) {
            existing = REFS.get(key);
            if (existing != null) {
                return existing == NULL_SENTINEL ? null : (T) existing;
            }

            T created = supplier.get();
            REFS.put(key, created == null ? NULL_SENTINEL : created);
            return created;
        }
    }

    /**
     * Gọi phương thức này khi FMLServerStoppingEvent hoặc RegistryEvent xảy ra
     * để giải phóng bộ nhớ.
     */
    public static void clear() {
        REFS.clear();
    }
}
