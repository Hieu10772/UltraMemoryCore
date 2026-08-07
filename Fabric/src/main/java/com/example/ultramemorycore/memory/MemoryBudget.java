package com.example.ultramemorycore.memory;

public final class MemoryBudget {

    private static final long MAX_HEAP_MB =
            Runtime.getRuntime().maxMemory() / 1024L / 1024L;

    private MemoryBudget() {}

    public static long maxHeapMB() {
        return MAX_HEAP_MB;
    }

    public static boolean isLowEnd() {
        return MAX_HEAP_MB <= 2048;
    }

    public static boolean isMidRange() {
        return MAX_HEAP_MB > 2048 && MAX_HEAP_MB <= 4096;
    }

    public static boolean isHighEnd() {
        return MAX_HEAP_MB > 4096;
    }
}
