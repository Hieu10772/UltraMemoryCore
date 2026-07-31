package com.example.ultramemorycore.cache;

import net.minecraft.util.Identifier;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IdentifierCache {

    private static final Map<String, SoftReference<Identifier>> CACHE =
            new ConcurrentHashMap<>();

    private IdentifierCache() {}

    public static Identifier cache(Identifier identifier) {
        if (identifier == null) {
            return null;
        }

        String key = identifier.toString();

        SoftReference<Identifier> ref = CACHE.get(key);
        if (ref != null) {
            Identifier cached = ref.get();
            if (cached != null) {
                return cached;
            }
        }

        CACHE.put(key, new SoftReference<>(identifier));
        return identifier;
    }

    public static void clear() {
        CACHE.clear();
    }
}
