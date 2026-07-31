package com.example.ultramemorycore.config;

import com.example.ultramemorycore.memory.MemoryProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UltraMemoryCoreConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("ultramemorycore.json").toFile();

    private MemoryProfile memoryProfile = MemoryProfile.IOS_POJAV;
    private boolean enableNBTPooling = true;
    private boolean enablePaletteDeduplication = true;
    private boolean enableBufferPooling = true;
    private boolean enableIdentifierCache = true;
    private boolean aggressiveChunkCacheCleanup = true;
    private int maxTemporaryBufferMB = 24;

    public static UltraMemoryCoreConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, UltraMemoryCoreConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UltraMemoryCoreConfig defaultConfig = new UltraMemoryCoreConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MemoryProfile getMemoryProfile() { return memoryProfile; }
    public boolean isEnableNBTPooling() { return enableNBTPooling; }
    public boolean isEnablePaletteDeduplication() { return enablePaletteDeduplication; }
    public boolean isEnableBufferPooling() { return enableBufferPooling; }
    public boolean isEnableIdentifierCache() { return enableIdentifierCache; }
    public boolean isAggressiveChunkCacheCleanup() { return aggressiveChunkCacheCleanup; }
    public int getMaxTemporaryBufferMB() { return maxTemporaryBufferMB; }
}
