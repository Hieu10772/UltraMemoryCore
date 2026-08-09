package com.example.ultramemorycore.cache;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;

public class BlockStatePaletteCache {
    private static final Int2ObjectOpenHashMap<IBlockState> CACHE = new Int2ObjectOpenHashMap<>();

    public static synchronized IBlockState get(int id) {
        return CACHE.get(id);
    }

    public static synchronized void put(int id, IBlockState state) {
        CACHE.put(id, state);
    }

    public static synchronized void clear() {
        CACHE.clear();
    }
}
