package com.example.ultramemorycore.mixin;

import net.minecraft.client.renderer.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BufferBuilder.class)
public class BufferBuilderMixin {
    // Intercepts client buffer allocations to utilize UploadBufferPool
}
