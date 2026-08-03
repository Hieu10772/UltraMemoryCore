package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;

public final class UltraMemoryCoreConfig {

    private boolean enabled = true;

    private MemoryProfile memoryProfile = MemoryProfile.DESKTOP;

    private int maxPaletteCache = 64;
    private int maxUploadBufferSize = 262_144;
    private long cacheKeepTimeMs = 15_000L;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MemoryProfile getMemoryProfile() {
        return memoryProfile;
    }

    public void setMemoryProfile(MemoryProfile profile) {
        this.memoryProfile = profile;
    }

    public int getMaxPaletteCache() {
        return maxPaletteCache;
    }

    public void setMaxPaletteCache(int value) {
        this.maxPaletteCache = value;
    }

    public int getMaxUploadBufferSize() {
        return maxUploadBufferSize;
    }

    public void setMaxUploadBufferSize(int value) {
        this.maxUploadBufferSize = value;
    }

    public long getCacheKeepTimeMs() {
        return cacheKeepTimeMs;
    }

    public void setCacheKeepTimeMs(long value) {
        this.cacheKeepTimeMs = value;
    }

    public static UltraMemoryCoreConfig load() {
        return new UltraMemoryCoreConfig();
    }

    public void save() {
    }
}
