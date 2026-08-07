package com.example.ultramemorycore.memory;

public enum MemoryProfile {

    AUTO(128, 512 * 1024, 30000),

    DESKTOP(256, 1024 * 1024, 60000),

    LOW_RAM_ANDROID(128, 512 * 1024, 30000),

    IOS_POJAV(64, 256 * 1024, 15000),

    CUSTOM(128, 512 * 1024, 30000);

    private final int maxPaletteCache;
    private final int maxUploadBufferSize;
    private final long poolTimeoutMs;

    MemoryProfile(
            int maxPaletteCache,
            int maxUploadBufferSize,
            long poolTimeoutMs
    ) {
        this.maxPaletteCache = maxPaletteCache;
        this.maxUploadBufferSize = maxUploadBufferSize;
        this.poolTimeoutMs = poolTimeoutMs;
    }

    public int getMaxPaletteCache() {
        return maxPaletteCache;
    }

    public int getMaxUploadBufferSize() {
        return maxUploadBufferSize;
    }

    public long getPoolTimeoutMs() {
        return poolTimeoutMs;
    }
}
