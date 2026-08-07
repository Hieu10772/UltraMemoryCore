package com.example.ultramemorycore.memory;

import net.minecraft.util.shape.VoxelShape;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public final class VoxelShapeCache {

    private static final ConcurrentHashMap<Integer, WeakReference<VoxelShape>> CACHE = 
    new ConcurrentHashMap<Integer, WeakReference<VoxelShape>>();


    // Giới hạn để chống phình khi bay Elytra
    private static final int MAX_ENTRIES = 2048;

    private VoxelShapeCache() {}

    public static VoxelShape deduplicate(VoxelShape shape) {

        if (shape == null) {
            return null;
        }

        // Dọn các WeakReference đã chết
        if ((CACHE.size() & 127) == 0) {
            sweep();
        }

        // Chống cache phình vô hạn
        if (CACHE.size() > MAX_ENTRIES) {
            CACHE.clear();
        }

        int hash = VoxelShapeHasher.hash(shape);

        WeakReference<VoxelShape> ref = CACHE.get(hash);

        if (ref != null) {
            VoxelShape cached = ref.get();

            if (cached != null) {
                return cached;
            }
        }

        CACHE.put(hash, new WeakReference<>(shape));

        return shape;
    }

    public static int size() {
        return CACHE.size();
    }

    public static void clear() {
        CACHE.clear();
    }

    public static void sweep() {
        WeakCacheSweeper.sweep(CACHE);
    }
}
