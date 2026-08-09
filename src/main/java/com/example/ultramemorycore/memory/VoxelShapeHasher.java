package com.example.ultramemorycore.memory;

import net.minecraft.util.math.AxisAlignedBB;

public final class VoxelShapeHasher {

    public static int hash(AxisAlignedBB shape) {
        if (shape == null) return 0;

        return shape.hashCode();
    }
}
