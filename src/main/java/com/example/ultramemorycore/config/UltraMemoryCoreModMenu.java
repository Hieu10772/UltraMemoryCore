package com.example.ultramemorycore.config;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.memory.MemoryProfile;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public final class UltraMemoryCoreModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return parent -> {
            UltraMemoryCoreConfig config = UltraMemoryCore.getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("UltraMemoryCore Settings"));

            // Lưu toàn bộ config 1 lần duy nhất khi bấm Save & Done
            builder.setSavingRunnable(config::save);

            ConfigCategory category = builder.getOrCreateCategory(
                    Component.literal("General")
            );

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // 1. Enable / Disable
            category.addEntry(entryBuilder.startBooleanToggle(
                            Component.literal("Enable UltraMemoryCore"),
                            config.isEnabled()
                    )
                    .setDefaultValue(true)
                    .setSaveConsumer(config::setEnabled)
                    .build());

            // 2. Device profile
            category.addEntry(entryBuilder.startEnumSelector(
                            Component.literal("Device Profile"),
                            MemoryProfile.class,
                            config.getMemoryProfile()
                    )
                    .setEnumNameProvider(profile -> {
                        if (profile == MemoryProfile.AUTO) return Component.literal("Auto");
                        if (profile == MemoryProfile.DESKTOP) return Component.literal("Desktop");
                        if (profile == MemoryProfile.LOW_RAM_ANDROID) return Component.literal("Android");
                        if (profile == MemoryProfile.IOS_POJAV) return Component.literal("iOS");
                        return Component.literal("Custom");
                    })
                    .setTooltipSupplier(profile -> {
                        if (profile == MemoryProfile.CUSTOM) {
                            return Optional.of(new Component[]{
                                    Component.literal("Enable custom memory settings."),
                                    Component.literal("Save and reopen to adjust custom options if changed.")
                            });
                        }

                        return Optional.of(new Component[]{
                                Component.literal("Palette Cache: " + profile.getMaxPaletteCache()),
                                Component.literal("Upload Buffer: " + (profile.getMaxUploadBufferSize() / 1024) + " KB"),
                                Component.literal("Cache Keep Time: " + profile.getPoolTimeoutMs() + " ms")
                        });
                    })
                    .setSaveConsumer(config::setMemoryProfile)
                    .build());

            // 3. Custom settings (Chỉ hiện khi profile hiện tại là CUSTOM)
            boolean custom = config.getMemoryProfile() == MemoryProfile.CUSTOM;

            if (custom) {
                // Max palette cache
                category.addEntry(entryBuilder.startIntField(
                                Component.literal("Max Palette Cache"),
                                config.getCustomMaxPaletteCache()
                        )
                        .setDefaultValue(128)
                        .setMin(32)
                        .setMax(2048)
                        .setSaveConsumer(config::setCustomMaxPaletteCache)
                        .build());

                // Upload buffer (KB)
                category.addEntry(entryBuilder.startIntField(
                                Component.literal("Max Upload Buffer (KB)"),
                                config.getCustomMaxUploadBufferSize() / 1024
                        )
                        .setDefaultValue(512)
                        .setMin(64)
                        .setMax(8192)
                        .setSaveConsumer(config::setCustomMaxUploadBufferSize)
                        .build());

                // Cache timeout
                category.addEntry(entryBuilder.startLongField(
                                Component.literal("Cache Keep Time (ms)"),
                                config.getCustomPoolTimeoutMs()
                        )
                        .setDefaultValue(30000L)
                        .setMin(5000L)
                        .setMax(600000L)
                        .setSaveConsumer(config::setCustomPoolTimeoutMs)
                        .build());
            }

            return builder.build();
        };
    }
}
