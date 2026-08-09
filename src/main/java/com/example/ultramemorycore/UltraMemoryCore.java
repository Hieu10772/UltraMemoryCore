package com.example.ultramemorycore;

import com.example.ultramemorycore.cache.BlockStatePaletteCache;
import com.example.ultramemorycore.cache.NbtStringPool;
import com.example.ultramemorycore.command.UmcCommand;
import com.example.ultramemorycore.config.UltraMemoryCoreConfig;
import com.example.ultramemorycore.memory.*;
import com.example.ultramemorycore.pool.ArrayPools;
import com.example.ultramemorycore.pool.PaletteArrayPool;
import com.example.ultramemorycore.pool.UploadBufferPool;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = UltraMemoryCore.MOD_ID,
    name = UltraMemoryCore.NAME,
    version = UltraMemoryCore.VERSION,
    clientSideOnly = true
)
public final class UltraMemoryCore {

    public static final String MOD_ID = "ultramemorycore";
    public static final String NAME = "UltraMemoryCore";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.Instance(MOD_ID)
    public static UltraMemoryCore instance;

    private static UltraMemoryCoreConfig config;
    private static MemoryProfile activeProfile;

    public static boolean isEnabled() {
        return config != null && config.isEnabled();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("ferritecore")) {
            String message = "Please remove FerriteCore to use UltraMemoryCore, Bye FerriteCore!";
            LOGGER.error("[UltraMemoryCore] {}", message);
            throw new IllegalStateException(message);
        }

        config = UltraMemoryCoreConfig.load(event.getSuggestedConfigurationFile());

        activeProfile = PlatformDetector.detectProfile(config.getMemoryProfile());

        LOGGER.info("[UltraMemoryCore] Initialized with Profile: {}", activeProfile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        ClientCommandHandler.instance.registerCommand(new UmcCommand());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isEnabled()) {
            return;
        }

        Minecraft client = Minecraft.getMinecraft();
        if (client.world == null || client.player == null) {
            return;
        }

        ChunkFlightTrimmer.tick();
        ChunkGovernor.tick();
        ChunkEvictionManager.tick(client);
        ChunkVisibilityTracker.tick(client);

        ElytraMemoryGuard.tick();
        PostFlightCleanup.tick(client);
        IdleMemoryBalancer.tick(client);

        ChunkColdStorage.tick(client);
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        trimAllCaches();
        LOGGER.info("[UltraMemoryCore] World disconnected - caches trimmed.");
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

        ChunkColdStorage.clear();

        LOGGER.info("[UltraMemoryCore] Memory pools and caches trimmed.");
    }
}
