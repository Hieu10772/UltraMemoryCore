package com.example.ultramemorycore.memory;

import net.minecraft.util.shape.VoxelShape;

public final class VoxelShapeHasher {

    public static int hash(VoxelShape shape) {
        if (shape == null) return 0;

        return shape.getBoundingBox().hashCode();
    }
}
