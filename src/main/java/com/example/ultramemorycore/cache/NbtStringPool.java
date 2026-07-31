package com.example.ultramemorycore.cache;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class NbtStringPool {
    private static final Map<String, WeakReference<String>> POOL = new WeakHashMap<>();
    private static final Map<String, String> HARD_PRELOADS = new ConcurrentHashMap<>();

    static {
        String[] preloads = {"id", "Count", "Damage", "tag", "Enchantments", "display", "Name", "components"};
        for (String key : preloads) {
            HARD_PRELOADS.put(key, key);
        }
    }

    public static synchronized String intern(String str) {
        if (str == null) return null;
        String hard = HARD_PRELOADS.get(str);
        if (hard != null) return hard;

        WeakReference<String> ref = POOL.get(str);
        if (ref != null) {
            String cached = ref.get();
            if (cached != null) return cached;
        }
        POOL.put(str, new WeakReference<>(str));
        return str;
    }

    public static synchronized void trim() {
        POOL.clear();
    }
}
