package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UltraMemoryCoreConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // Lấy thư mục /config chuẩn của Forge 1.12.2
    private static final Path CONFIG_PATH = Loader.instance().getConfigDir().toPath().resolve("ultramemorycore.json");

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
        if (Files.exists(CONFIG_PATH)) {
            // Sử dụng Reader chuẩn Java 8 truyền thẳng vào Gson (tiết kiệm bộ nhớ hơn đọc String)
            try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                UltraMemoryCoreConfig config = GSON.fromJson(reader, UltraMemoryCoreConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        UltraMemoryCoreConfig config = new UltraMemoryCoreConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            if (CONFIG_PATH.getParent() != null) {
                Files.createDirectories(CONFIG_PATH.getParent());
            }
            // Sử dụng Writer chuẩn Java 8 cho Gson
            try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
