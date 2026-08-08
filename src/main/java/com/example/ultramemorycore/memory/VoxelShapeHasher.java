package com.example.ultramemorycore.memory;

import net.minecraft.world.phys.shapes.VoxelShape;

public final class VoxelShapeHasher {

    public static int hash(VoxelShape shape) {
        if (shape == null) return 0;

        return shape.bounds().hashCode();
    }
}
