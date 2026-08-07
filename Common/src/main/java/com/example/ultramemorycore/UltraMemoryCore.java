package com.example.ultramemorycore;

public final class UltraMemoryCore {

    public static final String MOD_ID = "ultramemorycore";

    private static volatile boolean enabled = true;

    private UltraMemoryCore() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static void bootstrap() {
        enabled = true;
    }

    // Common không được phụ thuộc Minecraft / Fabric
    public static void trimAllCaches() {
        // xử lý cache phía Fabric
    }
}
