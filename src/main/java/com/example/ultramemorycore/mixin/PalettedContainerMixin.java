package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.pool.PaletteArrayPool;
import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PalettedContainer.class)
public class PalettedContainerMixin {
    @Inject(method = "copy", at = @At("HEAD"), require = 0)
    private void onCopy(CallbackInfoReturnable<?> cir) {
        // Intercept container copying to use pooled array buffers
    }
}
