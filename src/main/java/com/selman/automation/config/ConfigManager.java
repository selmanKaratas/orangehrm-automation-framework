package com.selman.automation.config;

import org.aeonbits.owner.ConfigCache;

public final class ConfigManager {

    private ConfigManager() {
        // Utility class — no instances
    }

    public static FrameworkConfig get() {
        return ConfigCache.getOrCreate(FrameworkConfig.class);
    }
}