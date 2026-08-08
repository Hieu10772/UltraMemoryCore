package com.example.ultramemorycore.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    // Intercepts component creation for memory reuse
}
