package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UltraMemoryCoreConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path DEFAULT_CONFIG_PATH = Loader.instance().getConfigDir().toPath().resolve("ultramemorycore.json");

    private boolean enabled = true;

    private MemoryProfile memoryProfile = MemoryProfile.DESKTOP;

    private int customMaxPaletteCache = 128;
    private int customMaxUploadBufferSize = 512 * 1024;
    private long customPoolTimeoutMs = 30_000L;

    private transient Path targetPath = DEFAULT_CONFIG_PATH;

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
        return load(null);
    }

    public static UltraMemoryCoreConfig load(File file) {
        Path path = (file != null) ? file.toPath() : DEFAULT_CONFIG_PATH;

        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                UltraMemoryCoreConfig config = GSON.fromJson(reader, UltraMemoryCoreConfig.class);
                if (config != null) {
                    config.targetPath = path;
                    return config;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        UltraMemoryCoreConfig config = new UltraMemoryCoreConfig();
        config.targetPath = path;
        config.save();
        return config;
    }

    public void save() {
        Path path = (targetPath != null) ? targetPath : DEFAULT_CONFIG_PATH;
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
