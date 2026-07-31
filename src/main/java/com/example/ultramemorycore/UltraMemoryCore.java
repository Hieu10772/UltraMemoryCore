package com.example.ultramemorycore;

import com.example.ultramemorycore.cache.BlockStatePaletteCache;
import com.example.ultramemorycore.cache.IdentifierCache;
import com.example.ultramemorycore.cache.NbtStringPool;
import com.example.ultramemorycore.command.UmcCommand;
import com.example.ultramemorycore.config.UltraMemoryCoreConfig;
import com.example.ultramemorycore.memory.MemoryProfile;
import com.example.ultramemorycore.memory.PlatformDetector;
import com.example.ultramemorycore.pool.ArrayPools;
import com.example.ultramemorycore.pool.PaletteArrayPool;
import com.example.ultramemorycore.pool.UploadBufferPool;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltraMemoryCore implements ModInitializer {
    public static final String MOD_ID = "ultramemorycore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static UltraMemoryCoreConfig config;
    private static MemoryProfile activeProfile;

    @Override
    public void onInitialize() {
        config = UltraMemoryCoreConfig.load();
        activeProfile = PlatformDetector.detectProfile(config.getMemoryProfile());

        LOGGER.info("[UltraMemoryCore] Initialized with Profile: {}", activeProfile);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            UmcCommand.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> trimAllCaches());
    }

    public static UltraMemoryCoreConfig getConfig() {
        return config;
    }

    public static MemoryProfile getActiveProfile() {
        return activeProfile;
    }

    public static void trimAllCaches() {
        NbtStringPool.trim();
        IdentifierCache.clear();
        BlockStatePaletteCache.clear();
        PaletteArrayPool.clear();
        ArrayPools.clearAll();
        UploadBufferPool.clear();
        LOGGER.info("[UltraMemoryCore] Memory pools and caches trimmed.");
    }
}
