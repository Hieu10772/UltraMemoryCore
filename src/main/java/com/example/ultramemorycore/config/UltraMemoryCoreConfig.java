package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;

public final class UltraMemoryCoreConfig {

    private boolean enabled = true;

    private MemoryProfile memoryProfile = MemoryProfile.AUTO;

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

    public static UltraMemoryCoreConfig load() {
        return new UltraMemoryCoreConfig();
    }

    public void save() {
    }
}
