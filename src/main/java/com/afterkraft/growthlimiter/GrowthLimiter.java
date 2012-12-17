package com.afterkraft.growthlimiter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;
import com.afterkraft.growthlimiter.listeners.GrowthLimiterBlockSpreadListener;
import com.afterkraft.growthlimiter.metrics.Metrics;

public final class GrowthLimiter extends JavaPlugin implements Listener {

    public boolean debug = false;
    public boolean grassBoolean = true;
    public boolean mycliumBoolean = true;
    public boolean vineBoolean = true;
    protected List<GrowthLimiterWorld> worlds;
    private GrowthLimiterConfig config;
    protected int configVersion = 0;
    private Logger log;
    private GrowthLimiterBlockSpreadListener listener;

    @Override
    public void onEnable() {
        log = getLogger();
        this.log.info("Ståarting GrowthLimiter!");
        loadConfig();
        enableListeners();
        loadMetrics();
        log.info("Done");
    }
    
    private void loadConfig() {
        log.info("Loading config file!");
        this.config = new GrowthLimiterConfig(this);
        this.config.loadConfig();
    }
    
    private void enableListeners() {
        log.info("Hooking listeners...");
        listener = new GrowthLimiterBlockSpreadListener(this, config);
        log.info("Listeners enabled!");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(listener);
        config.save();
    }

    public void loadMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            log.info("Couldn't start Metrics");
            // Failed to submit the stats :-(
        }
    }

}