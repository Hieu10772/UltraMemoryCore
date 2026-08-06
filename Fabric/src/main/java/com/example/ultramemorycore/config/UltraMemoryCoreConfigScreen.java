package com.example.ultramemorycore.config;

import com.example.ultramemorycore.UltraMemoryCore;
import com.example.ultramemorycore.memory.MemoryProfile;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;

public class UltraMemoryCoreConfigScreen extends Screen {

    private final Screen parent;
    private UltraMemoryCoreConfig config;

    protected UltraMemoryCoreConfigScreen(Screen parent) {
        super(Text.literal("UltraMemoryCore Settings"));
        this.parent = parent;
        this.config = UltraMemoryCore.getConfig();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 4;

        // Enable / Disable
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.isEnabled())
                        .build(centerX - 100, y, 200, 20,
                                Text.literal("Enabled"),
                                (button, value) -> config.setEnabled(value))
        );

        y += 28;

        // Memory Profile
        this.addDrawableChild(
                CyclingButtonWidget.builder(profile ->
                                Text.literal(profile.name()))
                        .values(MemoryProfile.values())
                        .initially(config.getMemoryProfile())
                        .build(centerX - 100, y, 200, 20,
                                Text.literal("Memory Profile"),
                                (button, value) -> config.setMemoryProfile(value))
        );

        y += 40;

        // Save button
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Save"), b -> {
                            config.save();
                            this.close();
                        })
                        .dimensions(centerX - 100, y, 95, 20)
                        .build()
        );

        // Cancel button
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Cancel"), b ->
                                this.close())
                        .dimensions(centerX + 5, y, 95, 20)
                        .build()
        );
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
