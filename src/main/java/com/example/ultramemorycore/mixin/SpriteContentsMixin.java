package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.client.SpriteAtlasUnloader;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.class)
public class SpriteContentsMixin {

    @Inject(
        method = "upload",
        at = @At("TAIL")
    )
    private void onUploadCompleted(CallbackInfo ci) {
        SpriteAtlasUnloader.freeMipmapNativeMemory((SpriteContents) (Object) this);
    }
}
