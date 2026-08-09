package com.example.ultramemorycore.client;

import com.example.ultramemorycore.memory.AdaptiveCacheManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public final class UltraMemoryClient {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Chỉ chạy ở cuối mỗi tick (tương đương END_CLIENT_TICK của Fabric)
        if (event.phase == TickEvent.Phase.END) {
            AdaptiveCacheManager.tick();
        }
    }
}
