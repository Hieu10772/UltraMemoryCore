package com.example.ultramemorycore.memory;

import net.minecraft.util.math.AxisAlignedBB;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public final class VoxelShapeCache {

    private static final ConcurrentHashMap<Integer, WeakReference<AxisAlignedBB>> CACHE =
            new ConcurrentHashMap<>();

    private static final int MAX_ENTRIES = 2048;

    private VoxelShapeCache() {}

    public static AxisAlignedBB deduplicate(AxisAlignedBB shape) {

        if (shape == null) {
            return null;
        }

        if ((CACHE.size() & 127) == 0) {
            sweep();
        }

        if (CACHE.size() > MAX_ENTRIES) {
            CACHE.clear();
        }

        int hash = VoxelShapeHasher.hash(shape);

        WeakReference<AxisAlignedBB> ref = CACHE.get(hash);

        if (ref != null) {
            AxisAlignedBB cached = ref.get();

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
