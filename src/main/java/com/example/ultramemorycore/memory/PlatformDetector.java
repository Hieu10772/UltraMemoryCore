package com.example.ultramemorycore.memory;

import java.util.Locale;

public final class PlatformDetector {

    private static final MemoryProfile DETECTED = detect();

    private PlatformDetector() {}

    public static MemoryProfile detectProfile(MemoryProfile configured) {
        return configured != null ? configured : DETECTED;
    }

    private static MemoryProfile detect() {

        String osName = System.getProperty("os.name", "")
                .toLowerCase(Locale.ROOT);

        boolean isPojav =
                System.getProperty("pojav.version") != null
                        || System.getenv("POJAV_BUILD") != null;

        if ((osName.contains("ios") || osName.contains("mac")) && isPojav) {
            return MemoryProfile.IOS_POJAV;
        }

        if (osName.contains("android") || isPojav) {
            return MemoryProfile.LOW_RAM_ANDROID;
        }

        return MemoryProfile.DESKTOP;
    }

    /**
     * Trên iOS + Pojav + MobileGLues việc reuse direct buffer thường phản tác dụng.
     */
    public static boolean disableUploadBufferPooling() {
    MemoryProfile profile = detectProfile(null);

    return profile == MemoryProfile.IOS_POJAV
            || profile == MemoryProfile.LOW_RAM_ANDROID;
}
}
