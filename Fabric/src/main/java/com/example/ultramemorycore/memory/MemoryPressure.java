package com.example.ultramemorycore.memory;

public final class MemoryPressure {

    private MemoryPressure() {}

    public static double usageRatio() {

        Runtime rt = Runtime.getRuntime();

        long used = rt.totalMemory() - rt.freeMemory();

        return (double) used / rt.maxMemory();
    }

    public static boolean isHigh() {
        return usageRatio() >= 0.75;
    }

    public static boolean isCritical() {
        return usageRatio() >= 0.90;
    }
}
