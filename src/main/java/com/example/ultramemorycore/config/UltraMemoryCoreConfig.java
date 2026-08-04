package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UltraMemoryCoreConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("ultramemorycore.json");

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
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                UltraMemoryCoreConfig config = GSON.fromJson(json, UltraMemoryCoreConfig.class);
                return config != null ? config : new UltraMemoryCoreConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        UltraMemoryCoreConfig config = new UltraMemoryCoreConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
