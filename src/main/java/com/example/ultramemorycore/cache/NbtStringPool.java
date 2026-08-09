package com.example.ultramemorycore.cache;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import java.util.HashMap;
import java.util.Map;

public final class NbtStringPool {
    // Bang tra cứu Lock-free cho cac NBT key phổ biến nhất 1.12.2
    private static final Map<String, String> HARD_PRELOADS = new HashMap<>();
    
    // Pool quản lý Weak Reference thread-safe tối ưu của Guava (có sẵn trong 1.12.2)
    @SuppressWarnings("deprecation")
    private static final Interner<String> WEAK_POOL = Interners.newWeakInterner();

    static {
        // NBT Keys chuẩn cho Minecraft 1.12.2 (Item, TileEntity, Entity, Chunk)
        String[] preloads = {
            "id", "Count", "Damage", "tag", "ench", "display", "Name", "Lore",
            "AttributeModifiers", "Unbreakable", "HideFlags", "CanDestroy", "CanPlaceOn",
            "SkullOwner", "pages", "title", "author", "BlockEntityTag", "EntityTag",
            "Pos", "Motion", "Rotation", "Health", "CustomName", "x", "y", "z", "Items"
        };
        for (String key : preloads) {
            HARD_PRELOADS.put(key, key);
        }
    }

    public static String intern(String str) {
        if (str == null) return null;

        // 1. Truy cập Lock-free cực nhanh cho key phổ biến
        String hard = HARD_PRELOADS.get(str);
        if (hard != null) return hard;

        // 2. Thread-safe weak interning qua Guava
        return WEAK_POOL.intern(str);
    }

    public static void trim() {
        // Guava Interner tự động dọn dẹp bộ nhớ thông qua ReferenceQueue khi GC chạy
    }
}
