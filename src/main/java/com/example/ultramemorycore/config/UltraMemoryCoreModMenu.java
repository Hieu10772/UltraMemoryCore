package com.example.ultramemorycore.config;

import com.example.ultramemorycore.UltraMemoryCore;
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

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("UltraMemoryCore Settings"));

            ConfigCategory category = builder.getOrCreateCategory(
                    Text.literal("General")
            );

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            category.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Enable UltraMemoryCore"),
                            UltraMemoryCore.getConfig().isEnabled()
                    )
                    .setDefaultValue(true)
                    .setSaveConsumer(value -> {
                        UltraMemoryCore.getConfig().setEnabled(value);
                        UltraMemoryCore.getConfig().save();
                    })
                    .build());

            return builder.build();
        };
    }
}
