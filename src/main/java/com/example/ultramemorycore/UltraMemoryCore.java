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
        // Kiểm tra mod xung đột FerriteCore trên Forge 1.12.2
        if (Loader.isModLoaded("ferritecore")) {
            String message = "Please remove FerriteCore to use UltraMemoryCore, Bye FerriteCore!";
            LOGGER.error("[UltraMemoryCore] {}", message);
            throw new IllegalStateException(message);
        }

        // Đọc cấu hình
        config = UltraMemoryCoreConfig.load(event.getSuggestedConfigurationFile());

        // Nhận diện cấu hình phần cứng
        activeProfile = PlatformDetector.detectProfile(config.getMemoryProfile());

        LOGGER.info("[UltraMemoryCore] Initialized with Profile: {}", activeProfile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Đăng ký Event Bus của Forge
        MinecraftForge.EVENT_BUS.register(this);

        // Đăng ký lệnh Client-side trên Forge 1.12.2
        ClientCommandHandler.instance.registerCommand(new UmcCommand());
    }

    // Tick toàn bộ hệ thống quản lý bộ nhớ Client
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isEnabled()) {
            return;
        }

        Minecraft client = Minecraft.getMinecraft();
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

        // Cold storage (unload chunk xa)
        ChunkColdStorage.tick(client);
    }

    // Khi người dùng ngắt kết nối khỏi World / Server
    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectedFromServerEvent event) {
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
