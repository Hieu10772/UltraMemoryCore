package com.example.ultramemorycore.config;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.memory.MemoryProfile;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public final class UltraMemoryCoreModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return parent -> {

            UltraMemoryCoreConfig config = UltraMemoryCore.getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("UltraMemoryCore Settings"));

            ConfigCategory category = builder.getOrCreateCategory(
                    Text.literal("General")
            );

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Enable / Disable
            category.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Enable UltraMemoryCore"),
                            config.isEnabled()
                    )
                    .setDefaultValue(true)
                    .setSaveConsumer(value -> {
                        config.setEnabled(value);
                        config.save();
                    })
                    .build());

            // Device selector
            category.addEntry(entryBuilder.startEnumSelector(
                            Text.literal("Device Profile"),
                            MemoryProfile.class,
                            config.getMemoryProfile()
                    )
                    .setDefaultValue(MemoryProfile.AUTO)
                    .setEnumNameProvider(profile -> switch (profile) {
                        case AUTO -> Text.literal("Auto");
                        case DESKTOP -> Text.literal("Desktop");
                        case LOW_RAM_ANDROID -> Text.literal("Android");
                        case IOS_POJAV -> Text.literal("iOS / Pojav");
                        case CUSTOM -> Text.literal("Custom");
                    })
                    .setSaveConsumer(value -> {
                        config.setMemoryProfile(value);
                        config.save();
                    })
                    .build());

            boolean custom = config.getMemoryProfile() == MemoryProfile.CUSTOM;

            // Max cache
            category.addEntry(entryBuilder.startIntField(
                            Text.literal("Max Cache Size"),
                            config.getCustomMaxPaletteCache()
                    )
                    .setDefaultValue(128)
                    .setMin(16)
                    .setMax(2048)
                    .setEditable(custom)
                    .setSaveConsumer(value -> {
                        config.setCustomMaxPaletteCache(value);
                        config.save();
                    })
                    .build());

            // Max buffer
            category.addEntry(entryBuilder.startIntField(
                            Text.literal("Max Upload Buffer Size (KB)"),
                            config.getCustomMaxUploadBufferSize() / 1024
                    )
                    .setDefaultValue(512)
                    .setMin(64)
                    .setMax(8192)
                    .setEditable(custom)
                    .setSaveConsumer(value -> {
                        config.setCustomMaxUploadBufferSize(value * 1024);
                        config.save();
                    })
                    .build());

            // Cache timeout
            category.addEntry(entryBuilder.startLongField(
                            Text.literal("Cache Cleanup Delay (ms)"),
                            config.getCustomPoolTimeoutMs()
                    )
                    .setDefaultValue(30000L)
                    .setMin(1000L)
                    .setMax(600000L)
                    .setEditable(custom)
                    .setSaveConsumer(value -> {
                        config.setCustomPoolTimeoutMs(value);
                        config.save();
                    })
                    .build());

            return builder.build();
        };
    }
}
