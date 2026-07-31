package com.example.ultramemorycore.command;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.pool.ArrayPools;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class UmcCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("umc")
            .requires((ServerCommandSource source) -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("stats").executes(ctx -> showStats(ctx.getSource())))
            .then(CommandManager.literal("gc").executes(ctx -> executeGC(ctx.getSource())))
            .then(CommandManager.literal("buffers").executes(ctx -> showBuffers(ctx.getSource())))
        );
    }

    private static int showStats(ServerCommandSource source) {
        Runtime r = Runtime.getRuntime();
        long usedMB = (r.totalMemory() - r.freeMemory()) / (1024 * 1024);
        long maxMB = r.maxMemory() / (1024 * 1024);
        source.sendFeedback(() -> Text.literal("§a[UMC Stats] §fUsed: " + usedMB + "MB / Max: " + maxMB + "MB | Profile: " + UltraMemoryCore.getActiveProfile()), false);
        return 1;
    }

    private static int executeGC(ServerCommandSource source) {
        UltraMemoryCore.trimAllCaches();
        System.gc();
        source.sendFeedback(() -> Text.literal("§a[UMC] §fCaches trimmed and System.gc() invoked."), false);
        return 1;
    }

    private static int showBuffers(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("§a[UMC Buffers] §fReused: " + ArrayPools.REUSED_COUNT.get() + " | Allocated: " + ArrayPools.ALLOCATED_COUNT.get()), false);
        return 1;
    }
}
