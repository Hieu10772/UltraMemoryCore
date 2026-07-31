package com.example.ultramemorycore.memory;

import net.minecraft.util.shape.VoxelShape;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public final class VoxelShapeCache {

    private static final ConcurrentHashMap<Integer, WeakReference<VoxelShape>> CACHE =
            new ConcurrentHashMap<>();

    private VoxelShapeCache() {}

    public static VoxelShape deduplicate(VoxelShape shape) {
        if (shape == null) {
            return null;
        }

        // Dùng hasher riêng thay vì toString().hashCode()
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
}
