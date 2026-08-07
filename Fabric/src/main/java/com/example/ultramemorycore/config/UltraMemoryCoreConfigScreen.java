package com.example.ultramemorycore.config;

import com.example.ultramemorycore.UltraMemoryCore; //[span_5](start_span)[span_5](end_span)
import com.example.ultramemorycore.memory.MemoryProfile; //[span_6](start_span)[span_6](end_span)
// Đổi import theo Mojang Mappings (26.2)
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UltraMemoryCoreConfigScreen extends Screen {

    private final Screen parent; //[span_7](start_span)[span_7](end_span)
    private UltraMemoryCoreConfig config; //[span_8](start_span)[span_8](end_span)

    // BẮT BỘC: Đổi từ 'protected' sang 'public' để ModMenu gọi được
    public UltraMemoryCoreConfigScreen(Screen parent) {
        super(Component.literal("UltraMemoryCore Settings"));
        this.parent = parent; //[span_9](start_span)[span_9](end_span)
        this.config = UltraMemoryCore.getConfig(); //[span_10](start_span)[span_10](end_span)
    }

    @Override
    protected void init() {
        int centerX = this.width / 2; //[span_11](start_span)[span_11](end_span)
        int y = this.height / 4; //[span_12](start_span)[span_12](end_span)

        // Enable / Disable
        this.addRenderableWidget(
                CycleButton.onOffBuilder(config.isEnabled())
                        .create(centerX - 100, y, 200, 20,
                                Component.literal("Enabled"),
                                (button, value) -> config.setEnabled(value))
        );

        y += 28; //[span_13](start_span)[span_13](end_span)

        // Memory Profile
        this.addRenderableWidget(
                CycleButton.builder(MemoryProfile::name)
                        .withValues(MemoryProfile.values())
                        .withInitialValue(config.getMemoryProfile())
                        .create(centerX - 100, y, 200, 20,
                                Component.literal("Memory Profile"),
                                (button, value) -> config.setMemoryProfile(value))
        );

        y += 40; //[span_14](start_span)[span_14](end_span)

        // Save button
        this.addRenderableWidget(
                Button.builder(Component.literal("Save"), b -> {
                            config.save(); //[span_15](start_span)[span_15](end_span)
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
