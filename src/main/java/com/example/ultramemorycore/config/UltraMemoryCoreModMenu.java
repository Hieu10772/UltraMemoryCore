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

            // Device profile
            category.addEntry(entryBuilder.startEnumSelector(
                            Text.literal("Device Profile"),
                            MemoryProfile.class,
                            config.getMemoryProfile()
                    )
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

if (!custom) {
    // chỉ hiển thị mô tả thay vì cho nhập số
    category.addEntry(entryBuilder.startTextDescription(
            Text.literal("Custom values are only editable when Device Profile = Custom")
    ).build());
}

            // Max palette cache
            category.addEntry(entryBuilder.startIntField(
                            Text.literal("Max Palette Cache"),
                            config.getCustomMaxPaletteCache()
                    )
                    .setDefaultValue(128)
                    .setMin(32)
                    .setMax(2048)
                    .setSaveConsumer(value -> {
                        if (custom) {
                            config.setCustomMaxPaletteCache(value);
                            config.save();
                        }
                    })
                    .build());

            // Upload buffer (KB)
            category.addEntry(entryBuilder.startIntField(
                            Text.literal("Max Upload Buffer (KB)"),
                            config.getCustomMaxUploadBufferSize() / 1024
                    )
                    .setDefaultValue(512)
                    .setMin(64)
                    .setMax(8192)
                    .setSaveConsumer(value -> {
                        if (custom) {
                            config.setCustomMaxUploadBufferSize(value * 1024);
                            config.save();
                        }
                    })
                    .build());

            // Cache timeout
            category.addEntry(entryBuilder.startLongField(
                            Text.literal("Cache Keep Time (ms)"),
                            config.getCustomPoolTimeoutMs()
                    )
                    .setDefaultValue(30000L)
                    .setMin(5000L)
                    .setMax(600000L)
                    .setSaveConsumer(value -> {
                        if (custom) {
                            config.setCustomPoolTimeoutMs(value);
                            config.save();
                        }
                    })
                    .build());

            return builder.build();
        };
    }
}
