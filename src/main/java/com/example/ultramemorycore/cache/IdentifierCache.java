package com.example.ultramemorycore.cache;

import net.minecraft.util.Identifier;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdentifierCache {
    private static final Map<String, SoftReference<Identifier>> CACHE = new ConcurrentHashMap<>();

    public static Identifier of(String namespace, String path) {
        String key = namespace + ":" + path;
        SoftReference<Identifier> ref = CACHE.get(key);
        if (ref != null) {
            Identifier id = ref.get();
            if (id != null) return id;
        }
        Identifier id = Identifier.of(namespace, path);
        CACHE.put(key, new SoftReference<>(id));
        return id;
    }

    public static void clear() {
        CACHE.clear();
    }
}
