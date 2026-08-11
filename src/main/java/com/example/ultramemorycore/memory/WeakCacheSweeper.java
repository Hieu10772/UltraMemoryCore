package com.example.ultramemorycore.memory;

import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

public final class WeakCacheSweeper {

    private static final List<ConcurrentHashMap<Integer, ? extends Reference<?>>> REGISTERED_CACHES = new CopyOnWriteArrayList<>();

    private WeakCacheSweeper() {}

    /**
     * Đăng ký một ConcurrentHashMap cần tự động quét rác định kỳ
     */
    public static void register(ConcurrentHashMap<Integer, ? extends Reference<?>> cache) {
        REGISTERED_CACHES.add(cache);
    }

    /**
     * Quét dọn thủ công một cache cụ thể
     */
    public static <T> void sweep(ConcurrentHashMap<Integer, ? extends Reference<T>> cache) {
        Iterator<? extends Map.Entry<Integer, ? extends Reference<T>>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, ? extends Reference<T>> entry = it.next();
            if (entry.getValue().get() == null) {
                it.remove();
            }
        }
    }

    /**
     * Được gọi mỗi tick từ UltraMemoryClient để dọn toàn bộ cache đã đăng ký
     */
    public static void tick() {
        for (ConcurrentHashMap<Integer, ? extends Reference<?>> cache : REGISTERED_CACHES) {
            sweep(cache);
        }
    }
}
