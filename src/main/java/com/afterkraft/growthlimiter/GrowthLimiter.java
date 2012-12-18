/*
 * Copyright 2012 Gabriel Harris-Rouquette. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this list of
 *     conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list
 *     of conditions and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

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
    public boolean myceliumBoolean = true;
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
        saveConfig();
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