package com.example.ultramemorycore.config;

public final class UltraMemoryCoreConfig {

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static UltraMemoryCoreConfig load() {
        return new UltraMemoryCoreConfig();
    }

    public void save() {
    }
}
