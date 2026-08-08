package com.example.ultramemorycore.config;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.memory.MemoryProfile;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UltraMemoryCoreConfigScreen extends Screen {

    private final Screen parent;
    private UltraMemoryCoreConfig config;

    protected UltraMemoryCoreConfigScreen(Screen parent) {
        super(Component.literal("UltraMemoryCore Settings"));
        this.parent = parent;
        this.config = UltraMemoryCore.getConfig();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 4;

        // Enable / Disable
        this.addRenderableWidget(
                CycleButton.onOffBuilder(config.isEnabled())
                        .create(centerX - 100, y, 200, 20,
                                Component.literal("Enabled"),
                                (button, value) -> config.setEnabled(value))
        );

        y += 28;

        // Memory Profile
        this.addRenderableWidget(
                CycleButton.builder((MemoryProfile profile) ->
                                Component.literal(profile.name()))
                        .withValues(MemoryProfile.values())
                        .withInitialValue(config.getMemoryProfile())
                        .create(centerX - 100, y, 200, 20,
                                Component.literal("Memory Profile"),
                                (button, value) -> config.setMemoryProfile(value))
        );

        y += 40;

        // Save button
        this.addRenderableWidget(
                Button.builder(Component.literal("Save"), b -> {
                            config.save();
                            this.onClose();
                        })
                        .bounds(centerX - 100, y, 95, 20)
                        .build()
        );

        // Cancel button
        this.addRenderableWidget(
                Button.builder(Component.literal("Cancel"), b ->
                                this.onClose())
                        .bounds(centerX + 5, y, 95, 20)
                        .build()
        );
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}
