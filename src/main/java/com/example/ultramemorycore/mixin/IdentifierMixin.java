package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.cache.IdentifierCache;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Identifier.class)
public class IdentifierMixin {
    @Inject(method = "of(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true, require = 0)
    private static void onOf(String namespace, String path, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(IdentifierCache.of(namespace, path));
    }
}
