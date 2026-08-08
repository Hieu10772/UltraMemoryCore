package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.cache.NbtStringPool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CompoundTag.class)
public class NbtCompoundMixin {
    @ModifyVariable(method = "put", at = @At("HEAD"), argsOnly = true, require = 0)
    private String internNbtKey(String key) {
        return NbtStringPool.intern(key);
    }
}
