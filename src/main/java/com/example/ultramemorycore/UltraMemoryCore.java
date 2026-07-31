package com.example.ultramemorycore;

import com.example.ultramemorycore.memory.ChunkGovernor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.example.ultramemorycore.memory.FastPropertyMap;
import com.example.ultramemorycore.memory.UltraFastPropertyMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.example.ultramemorycore.cache.BlockStatePaletteCache;
import com.example.ultramemorycore.cache.NbtStringPool;
import com.example.ultramemorycore.command.UmcCommand;
import com.example.ultramemorycore.config.UltraMemoryCoreConfig;
import com.example.ultramemorycore.memory.MemoryProfile;
import com.example.ultramemorycore.memory.PlatformDetector;
import com.example.ultramemorycore.memory.SharedPropertyMap;
import com.example.ultramemorycore.memory.VoxelShapeCache;
import com.example.ultramemorycore.pool.ArrayPools;
import com.example.ultramemorycore.pool.PaletteArrayPool;
import com.example.ultramemorycore.pool.UploadBufferPool;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UltraMemoryCore implements ModInitializer {

    public static final String MOD_ID = "ultramemorycore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static UltraMemoryCoreConfig config;
    private static MemoryProfile activeProfile;

    @Override
public void onInitialize() {

    config = UltraMemoryCoreConfig.load();

    activeProfile =
            PlatformDetector.detectProfile(config.getMemoryProfile());

    LOGGER.info(
            "[UltraMemoryCore] Initialized with Profile: {}",
            activeProfile
    );

    CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                    UmcCommand.register(dispatcher)
    );

    // Đóng hẳn Minecraft
    ClientLifecycleEvents.CLIENT_STOPPING.register(
            client -> trimAllCaches()
    );

    // Rời world về menu chính
    ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
        trimAllCaches();

        LOGGER.info(
                "[UltraMemoryCore] World disconnected - caches trimmed."
        );
    });
    // Tick governor mỗi frame client
ClientTickEvents.END_CLIENT_TICK.register(client -> {
    ChunkGovernor.tick();
});
}

    public static UltraMemoryCoreConfig getConfig() {
        return config;
    }

    public static MemoryProfile getActiveProfile() {
        return activeProfile;
    }

    /**
     * Dọn toàn bộ cache và pool để giảm memory spike sau khi thoát world.
     */
    public static void trimAllCaches() {
        UltraFastPropertyMap.clear();
        
        FastPropertyMap.clear();

        SharedPropertyMap.trim();

        VoxelShapeCache.clear();

        NbtStringPool.trim();

        BlockStatePaletteCache.clear();

        PaletteArrayPool.clear();

        ArrayPools.clearAll();

        UploadBufferPool.clear();

        LOGGER.info(
                "[UltraMemoryCore] Memory pools and caches trimmed."
        );
    }
}
