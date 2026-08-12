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

    public static void register(ConcurrentHashMap<Integer, ? extends Reference<?>> cache) {
        REGISTERED_CACHES.add(cache);
    }

    public static void sweep(ConcurrentHashMap<Integer, ? extends Reference<?>> cache) {
        Iterator<? extends Map.Entry<Integer, ? extends Reference<?>>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, ? extends Reference<?>> entry = it.next();
            if (entry.getValue() != null && entry.getValue().get() == null) {
                it.remove();
            }
        }
    }

    public static void tick() {
        for (ConcurrentHashMap<Integer, ? extends Reference<?>> cache : REGISTERED_CACHES) {
            sweep(cache);
        }
    }
}
