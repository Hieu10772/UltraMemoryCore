package com.example.ultramemorycore.client;

import com.example.ultramemorycore.mixin.SpriteContentsAccessor;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;

public class SpriteAtlasUnloader {

    public static void freeMipmapNativeMemory(SpriteContents contents) {
        if (contents == null) return;

        try {
            NativeImage[] mipmaps = ((SpriteContentsAccessor) contents).getMipmapLevels();

            if (mipmaps != null && mipmaps.length > 1) {
                for (int i = 1; i < mipmaps.length; i++) {
                    if (mipmaps[i] != null) {
                        mipmaps[i].close();
                        mipmaps[i] = null;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
