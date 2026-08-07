package com.example.ultramemorycore;

public final class UltraMemoryCore {

    public static final String MOD_ID = "ultramemorycore";

    private static volatile boolean enabled = true;

    private UltraMemoryCore() {}

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static void bootstrap() {
        enabled = true;
    }

    public static void trimAllCaches() {
        // để trống hoặc gọi các cache nằm trong Common
    }
}
