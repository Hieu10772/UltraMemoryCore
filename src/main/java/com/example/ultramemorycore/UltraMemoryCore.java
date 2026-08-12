package com.example.ultramemorycore;

import com.example.ultramemorycore.cache.BlockStatePaletteCache;
import com.example.ultramemorycore.cache.NbtStringPool;
import com.example.ultramemorycore.command.UmcCommand;
import com.example.ultramemorycore.config.UltraMemoryCoreConfig;
import com.example.ultramemorycore.memory.AdaptiveCacheManager;
import com.example.ultramemorycore.memory.ChunkColdStorage;
import com.example.ultramemorycore.memory.ChunkEvictionManager;
import com.example.ultramemorycore.memory.ChunkFlightTrimmer;
import com.example.ultramemorycore.memory.ChunkGovernor;
import com.example.ultramemorycore.memory.ChunkVisibilityTracker;
import com.example.ultramemorycore.memory.ElytraMemoryGuard;
import com.example.ultramemorycore.memory.FastPropertyMap;
import com.example.ultramemorycore.memory.IdleMemoryBalancer;
import com.example.ultramemorycore.memory.MemoryProfile;
import com.example.ultramemorycore.memory.PlatformDetector;
import com.example.ultramemorycore.memory.PostFlightCleanup;
import com.example.ultramemorycore.memory.SharedPropertyMap;
import com.example.ultramemorycore.memory.UltraFastPropertyMap;
import com.example.ultramemorycore.memory.VoxelShapeCache;
import com.example.ultramemorycore.memory.WeakCacheSweeper;
import com.example.ultramemorycore.pool.ArrayPools;
import com.example.ultramemorycore.pool.NettyDirectBufferPool;
import com.example.ultramemorycore.pool.PaletteArrayPool;
import com.example.ultramemorycore.pool.UploadBufferPool;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UltraMemoryCore implements ModInitializer {

    public static final String MOD_ID = "ultramemorycore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static UltraMemoryCoreConfig config;
    private static MemoryProfile activeProfile;

    public static boolean isEnabled() {
        return config != null && config.isEnabled();
    }

    @Override
    public void onInitialize() {
        // Không cho chạy cùng FerriteCore
        if (FabricLoader.getInstance().isModLoaded("ferritecore")) {
            String message = "Please remove FerriteCore to use UltraMemoryCore, Bye FerriteCore!";
            LOGGER.error("[UltraMemoryCore] {}", message);
            throw new IllegalStateException(message);
        }

        config = UltraMemoryCoreConfig.load();
        activeProfile = PlatformDetector.detectProfile(config.getMemoryProfile());

        LOGGER.info("[UltraMemoryCore] Initialized with Profile: {}", activeProfile);

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> UmcCommand.register(dispatcher)
        );

        // Đóng hẳn Minecraft
        ClientLifecycleEvents.CLIENT_STOPPING.register(
                client -> trimAllCaches()
        );

        // Rời world về menu chính
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            trimAllCaches();
            LOGGER.info("[UltraMemoryCore] World disconnected - caches trimmed.");
        });

        // Tick toàn bộ hệ thống quản lý bộ nhớ client
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!UltraMemoryCore.isEnabled()) {
                return;
            }

            if (client.world == null || client.player == null) {
                return;
            }

            // Adaptive & Weak cache cleanup
            AdaptiveCacheManager.tick();
            WeakCacheSweeper.tick();

            // Chunk streaming
            ChunkFlightTrimmer.tick();
            ChunkGovernor.tick();
            ChunkEvictionManager.tick(client);
            ChunkVisibilityTracker.tick(client);

            // Elytra / idle cleanup
            ElytraMemoryGuard.tick();
            PostFlightCleanup.tick(client);
            IdleMemoryBalancer.tick(client);

            // Cold storage (unload chunk xa)
            ChunkColdStorage.tick(client);
        });
    }

    public static UltraMemoryCoreConfig getConfig() {
        return config;
    }

    public static MemoryProfile getActiveProfile() {
        return activeProfile;
    }

    public static void trimAllCaches() {
        UltraFastPropertyMap.clear();
        FastPropertyMap.clear();
        SharedPropertyMap.clear();

        VoxelShapeCache.clear();

        NbtStringPool.trim();
        BlockStatePaletteCache.clear();
        PaletteArrayPool.clear();

        ArrayPools.clearAll();
        UploadBufferPool.clear();

        NettyDirectBufferPool.clear();

        ChunkColdStorage.clear();

        LOGGER.info("[UltraMemoryCore] Memory pools and caches trimmed.");
    }
}
