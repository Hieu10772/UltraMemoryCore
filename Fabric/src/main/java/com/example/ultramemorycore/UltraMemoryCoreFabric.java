package com.example.ultramemorycore;

import com.example.ultramemorycore.command.UmcCommand;
import com.example.ultramemorycore.config.UltraMemoryCoreConfig;
import com.example.ultramemorycore.memory.ChunkColdStorage;
import com.example.ultramemorycore.memory.ChunkEvictionManager;
import com.example.ultramemorycore.memory.ChunkFlightTrimmer;
import com.example.ultramemorycore.memory.ChunkGovernor;
import com.example.ultramemorycore.memory.ChunkVisibilityTracker;
import com.example.ultramemorycore.memory.ElytraMemoryGuard;
import com.example.ultramemorycore.memory.IdleMemoryBalancer;
import com.example.ultramemorycore.memory.MemoryProfile;
import com.example.ultramemorycore.memory.PlatformDetector;
import com.example.ultramemorycore.memory.PostFlightCleanup;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UltraMemoryCoreFabric implements ModInitializer {

    public static final Logger LOGGER =
            LoggerFactory.getLogger(UltraMemoryCore.MOD_ID);

    private static UltraMemoryCoreConfig config;
    private static MemoryProfile activeProfile;

    @Override
    public void onInitialize() {

        // Chặn FerriteCore
        if (FabricLoader.getInstance().isModLoaded("ferritecore")) {
            String message =
                    "Please remove FerriteCore to use UltraMemoryCore.";

            LOGGER.error("[UltraMemoryCore] {}", message);
            throw new IllegalStateException(message);
        }

        config = UltraMemoryCoreConfig.load();

        activeProfile =
                PlatformDetector.detectProfile(config.getMemoryProfile());

        // Khởi tạo lõi Common
        UltraMemoryCore.bootstrap();
        UltraMemoryCore.setEnabled(config.isEnabled());

        LOGGER.info(
                "[UltraMemoryCore] Initialized with profile: {}",
                activeProfile
        );

        // Command
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        UmcCommand.register(dispatcher)
        );

        // Đóng hẳn Minecraft
ClientLifecycleEvents.CLIENT_STOPPING.register(
        client -> trimClientCaches()
);

// Rời world về menu chính
ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
    trimClientCaches();

    LOGGER.info(
            "[UltraMemoryCore] World disconnected - caches trimmed."
    );
});

        // Tick hệ thống quản lý bộ nhớ
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (!UltraMemoryCore.isEnabled()) {
                return;
            }

            if (client.world == null || client.player == null) {
                return;
            }

            // Chunk streaming
            ChunkFlightTrimmer.tick();
            ChunkGovernor.tick();
            ChunkEvictionManager.tick(client);
            ChunkVisibilityTracker.tick(client);

            // Elytra / idle cleanup
            ElytraMemoryGuard.tick();
            PostFlightCleanup.tick(client);
            IdleMemoryBalancer.tick(client);

            // Cold storage
            ChunkColdStorage.tick(client);
        });
    }

    public static UltraMemoryCoreConfig getConfig() {
        return config;
    }

    public static MemoryProfile getActiveProfile() {
        return activeProfile;
    }
    
    private static void trimClientCaches() {
    UltraFastPropertyMap.clear();
    FastPropertyMap.clear();
    SharedPropertyMap.clear();

    VoxelShapeCache.clear();

    NbtStringPool.trim();
    BlockStatePaletteCache.clear();

    PaletteArrayPool.clear();
    ArrayPools.clearAll();
    UploadBufferPool.clear();

    ChunkColdStorage.clear();

    LOGGER.info("[UltraMemoryCore] Memory pools and caches trimmed.");
}
}
