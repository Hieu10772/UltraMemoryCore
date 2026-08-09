package com.example.ultramemorycore.command;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.memory.SharedPropertyMap;
import com.example.ultramemorycore.pool.ArrayPools;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class UmcCommand extends CommandBase {

    @Override
    public String getName() {
        return "umc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/umc <stats|gc|buffers>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString("§cCú pháp: " + getUsage(sender)));
            return;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "stats":
                showStats(sender);
                break;
            case "gc":
                executeGC(sender);
                break;
            case "buffers":
                showBuffers(sender);
                break;
            default:
                sender.sendMessage(new TextComponentString("§cLệnh không hợp lệ. Cú pháp: " + getUsage(sender)));
                break;
        }
    }

    private void showStats(ICommandSender sender) {
        Runtime r = Runtime.getRuntime();
        long usedMB = (r.totalMemory() - r.freeMemory()) / (1024 * 1024);
        long maxMB = r.maxMemory() / (1024 * 1024);

        sender.sendMessage(new TextComponentString(
                "§a[UMC Stats] §fUsed: " + usedMB +
                "MB / Max: " + maxMB +
                "MB | SharedProperties: " +
                SharedPropertyMap.size()
        ));
    }

    private void executeGC(ICommandSender sender) {
        UltraMemoryCore.trimAllCaches();
        System.gc();

        sender.sendMessage(new TextComponentString(
                "§a[UMC] §fCaches trimmed and System.gc() invoked."
        ));
    }

    private void showBuffers(ICommandSender sender) {
        sender.sendMessage(new TextComponentString(
                "§a[UMC Buffers] §fReused: " +
                ArrayPools.REUSED_COUNT.get() +
                " | Allocated: " +
                ArrayPools.ALLOCATED_COUNT.get()
        ));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfMatchingStrings(args, "stats", "gc", "buffers");
        }
        return Collections.emptyList();
    }
}
