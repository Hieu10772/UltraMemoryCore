package com.example.ultramemorycore.cache;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStatePaletteCache {
    private static final Int2ObjectOpenHashMap<BlockState> CACHE = new Int2ObjectOpenHashMap<>();

    public static synchronized BlockState get(int id) {
        return CACHE.get(id);
    }

    public static synchronized void put(int id, BlockState state) {
        CACHE.put(id, state);
    }

    public static synchronized void clear() {
        CACHE.clear();
    }
}
