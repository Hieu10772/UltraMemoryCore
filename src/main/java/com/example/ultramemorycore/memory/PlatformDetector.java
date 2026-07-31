package com.example.ultramemorycore.memory;

import java.util.Locale;

public class PlatformDetector {
    public static MemoryProfile detectProfile(MemoryProfile configured) {
        if (configured != null) return configured;

        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String javaVendor = System.getProperty("java.vendor", "").toLowerCase(Locale.ROOT);
        boolean isPojav = System.getProperty("pojav.version") != null || System.getenv("POJAV_BUILD") != null;

        if (osName.contains("ios") || osName.contains("mac") && isPojav) {
            return MemoryProfile.IOS_POJAV;
        } else if (osName.contains("android") || isPojav) {
            return MemoryProfile.LOW_RAM_ANDROID;
        }
        return MemoryProfile.DESKTOP;
    }
}
