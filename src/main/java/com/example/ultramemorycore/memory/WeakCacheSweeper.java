package com.example.ultramemorycore.memory;

import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class WeakCacheSweeper {

    private WeakCacheSweeper() {}

    public static <T> void sweep(
            ConcurrentHashMap<Integer, ? extends Reference<T>> cache) {

        Iterator<? extends Map.Entry<Integer, ? extends Reference<T>>> it =
                cache.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<Integer, ? extends Reference<T>> entry = it.next();

            if (entry.getValue().get() == null) {
                it.remove();
            }
        }
    }
}
