package com.selman.automation.config;

import org.aeonbits.owner.Config;


@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:config.properties"
})
public interface FrameworkConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("browser")
    @DefaultValue("chromium")
    String browser();

    @Key("headless")
    @DefaultValue("true")
    boolean headless();

    @Key("default.username")
    String defaultUsername();

    @Key("default.password")
    String defaultPassword();

    @Key("timeout.ms")
    @DefaultValue("30000")
    int timeoutMs();
}