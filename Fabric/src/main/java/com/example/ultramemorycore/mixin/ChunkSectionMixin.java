package com.example.ultramemorycore.mixin;

import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkSection.class)
public class ChunkSectionMixin {
    // Hooks for chunk section memory optimization
}
