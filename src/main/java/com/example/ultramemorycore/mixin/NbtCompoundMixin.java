package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.cache.NbtStringPool;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(NBTTagCompound.class)
public class NbtCompoundMixin {

    @ModifyVariable(method = "setTag", at = @At("HEAD"), argsOnly = true, require = 0)
    private String internNbtKey(String key) {
        return NbtStringPool.intern(key);
    }
}
