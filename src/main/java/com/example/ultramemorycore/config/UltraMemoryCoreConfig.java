package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;

public final class UltraMemoryCoreConfig {

    private boolean enabled = true;

    private MemoryProfile memoryProfile = MemoryProfile.DESKTOP;

    private int customMaxPaletteCache = 128;
    private int customMaxUploadBufferSize = 512 * 1024;
    private long customPoolTimeoutMs = 30_000L;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MemoryProfile getMemoryProfile() {
        return memoryProfile;
    }

    public void setMemoryProfile(MemoryProfile memoryProfile) {
        this.memoryProfile = memoryProfile;
    }

    public int getCustomMaxPaletteCache() {
        return customMaxPaletteCache;
    }

    public void setCustomMaxPaletteCache(int value) {
        this.customMaxPaletteCache = value;
    }

    public int getCustomMaxUploadBufferSize() {
        return customMaxUploadBufferSize;
    }

    public void setCustomMaxUploadBufferSize(int value) {
        this.customMaxUploadBufferSize = value;
    }

    public long getCustomPoolTimeoutMs() {
        return customPoolTimeoutMs;
    }

    public void setCustomPoolTimeoutMs(long value) {
        this.customPoolTimeoutMs = value;
    }

    public static UltraMemoryCoreConfig load() {
        return new UltraMemoryCoreConfig();
    }

    public void save() {
    }
}
