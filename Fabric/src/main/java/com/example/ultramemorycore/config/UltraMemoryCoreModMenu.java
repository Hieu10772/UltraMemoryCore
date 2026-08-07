package com.example.ultramemorycore.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory; //[span_17](start_span)[span_17](end_span)
import com.terraformersmc.modmenu.api.ModMenuApi; //[span_18](start_span)[span_18](end_span)

public final class UltraMemoryCoreModMenu implements ModMenuApi { //[span_19](start_span)[span_19](end_span)

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() { //[span_20](start_span)[span_20](end_span)
        return parent -> new UltraMemoryCoreConfigScreen(parent);
    }
}
