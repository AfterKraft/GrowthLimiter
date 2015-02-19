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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;

public class GrowthLimiterConfig {

    protected static boolean debug = false;
    protected static List<GrowthLimiterWorld> Worlds;
    private static List<String> stringWorlds;
    private static boolean firstTimeLoading = true;
    private GrowthLimiter plugin;
    private final Logger log;
    private FileConfiguration config;
    private int configConvertVersion = 0;

    private final int configVersion = 4;

    public GrowthLimiterConfig(GrowthLimiter plugin) {
        this.plugin = plugin;
        log = plugin.getLogger();
    }

    public void loadConfig() {
        File configDir = plugin.getDataFolder();
        if (!configDir.exists()) {
            configDir.mkdir();
        }

        Worlds = new ArrayList<GrowthLimiterWorld>();
        // Check for existance of config file
        config = plugin.getConfig();

        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveDefaultConfig();
            growthConfigParser();
        } else {
            try {
                plugin.getConfig();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                firstTimeLoading = false;
                // Determine whether the configuration file needs to be
                // converted or not
                if (config.getInt("Version") == configVersion) {
                    log.info("Loading Configuration file");
                    configConvertVersion = configVersion;
                    // Parse config now
                    growthConfigParser();
                } else if (config.contains("Version")) {
                    configConvertVersion = config.getInt("Version");
                    log.info("ConfigurationConvertVersion is: " + configConvertVersion);
                    legacyConfigParser();
                    legacyConfigConverter();
                    plugin.saveConfig();
                    growthConfigParser();
                } else {
                    log.info("Legacy Configuration detected! Reading old config to prepare for conversion!");
                    configConvertVersion = 2;
                    legacyConfigParser();
                    legacyConfigConverter();
                    plugin.saveConfig();
                    growthConfigParser();
                }
                log.info("The following worlds are configured: " + Worlds);
                listWorldConfigs();
                plugin.saveConfig();
            }
        }
        plugin.debug = debug;

    }

    /**
     * This is used to parse an old config and convert to the new format
     * Effective as of v0.3
     */
    private void legacyConfigParser() {
        log.info("Started the Legacy Config Parser!");
        switch (configConvertVersion) {
        case 1: // Parse for GrowthLimiter v0.1
            break;
        case 2: // Parse for GrowthLimiter v0.2
            log.info("Detecting GrowthLimiter v0.2!, Parsing!");
            debug = config.getBoolean("Debug");
            stringWorlds = config.getStringList("Worlds");
            if (!Worlds.isEmpty()) {
                Worlds.clear();
                for (String stringWorldName : stringWorlds) {
                    GrowthLimiterWorld temp = new GrowthLimiterWorld();
                    temp.worldName = stringWorldName;
                    Worlds.add(temp);
                }
            }
            for (String worldString : stringWorlds) {
                log.info("Now parsing " + worldString + " for conversion!");
                GrowthLimiterWorld world = new GrowthLimiterWorld();
                // Retrieve the World Name
                world.worldName = worldString;
                // Default previously non-existant boolean
                world.grassBoolean = true;
                // Retrieve Global Growth Value
                world.grassGrowth = config.getInt("Growth.Grass");
                // Default previously non-existant boolean
                world.myceliumBoolean = true;
                // Retrieve Mycelium Growth Value
                world.myceliumGrowth = config.getInt("Growth.Mycelium");
                // Default previously non-existant boolean
                world.vineBoolean = false;
                // Default previously non-existant int
                world.vineGrowth = 50;
                // Default previously non-existant boolean
                world.vineMaxDistance = 10;
                Worlds.add(world);
                log.info("Done!");
            }
            break;
        case 3: // Parse for GrowthLimiter v0.3
            if (config.getBoolean("Debug")) {
                debug = true;
            }
            stringWorlds = new ArrayList<String>();
            stringWorlds.addAll(plugin.getConfig().getConfigurationSection("Worlds").getKeys(false));
            if (!Worlds.isEmpty())
                Worlds.clear();

            for (String stringWorldName : stringWorlds) {
                GrowthLimiterWorld temp = new GrowthLimiterWorld();
                temp.worldName = stringWorldName;
                Worlds.add(temp);
            }
            plugin.debug = config.getBoolean("Debug");
            plugin.grassBoolean = config.getBoolean("Enabled.Grass");
            plugin.myceliumBoolean = config.getBoolean("Enabled.Mycelium");
            plugin.vineBoolean = config.getBoolean("Enabled.Vine");
            plugin.wheatBoolean = true;
            plugin.configVersion = config.getInt("Version");
            for (GrowthLimiterWorld world : Worlds) {
                world.grassGrowth = config.getInt("Worlds." + world.worldName + ".Grass");
                world.myceliumGrowth = config.getInt("Worlds." + world.worldName + ".Mycelium");
                world.vineGrowth = config.getInt("Worlds." + world.worldName + ".Vine");
                world.vineMaxDistance = config.getInt("Worlds." + world.worldName + ".Vine-Distance");
                world.wheatGrowth = 50;
                world.grassGrowthPercent = (double) world.grassGrowth / 100;
                world.myceliumGrowthPercent = (double) world.myceliumGrowth / 100;
                world.vineGrowthPercent = (double) world.vineGrowth / 100;
                world.wheatGrowthPercent = (double) world.wheatGrowth / 100;
            }
            plugin.worlds = Worlds;
            break;
        case 4:
            growthConfigParser();
            break;
        }

    }

    /**
     * Parse for current config if passed not-legacy-check Effective as of v0.3
     * (Config version 4)
     */
    private void growthConfigParser() {
        if (config.getBoolean("Debug")) {
            debug = true;
        }
        stringWorlds = new ArrayList<String>();
        stringWorlds.addAll(plugin.getConfig().getConfigurationSection("Worlds").getKeys(false));
        if (!Worlds.isEmpty())
            Worlds.clear();

        for (String stringWorldName : stringWorlds) {
            GrowthLimiterWorld temp = new GrowthLimiterWorld();
            temp.worldName = stringWorldName;
            Worlds.add(temp);
        }
        plugin.debug = config.getBoolean("Debug");
        // Grass Boolean
        plugin.grassBoolean = config.getBoolean("Enabled.Grass");
        if (debug) {
            if (plugin.grassBoolean) {
                log.info("Grass control is enabled!");
            } else {
                log.info("Grass control is disabled!");
            }
        }
        // Mycelium Boolean
        plugin.myceliumBoolean = config.getBoolean("Enabled.Mycelium");
        if (debug) {
            if (plugin.myceliumBoolean) {
                log.info("Mycelium control is enabled!");
            } else {
                log.info("mycelium control is disabled!");
            }
        }
        // Vine Boolean
        plugin.vineBoolean = config.getBoolean("Enabled.Vine");
        if (debug) {
            if (plugin.vineBoolean) {
                log.info("Vine control is enabled!");
            } else {
                log.info("Vine control is disabled!");
            }
        }
        // Wheat Boolean
        plugin.wheatBoolean = config.getBoolean("Enabled.Wheat");
        if (debug) {
            if (plugin.wheatBoolean) {
                log.info("Wheat Control is enabled!");
            } else {
                log.info("Wheat control is disabled!");
            }
        }
        plugin.sugarBoolean = config.getBoolean("Enabled.Sugar");
        if (debug) {
            if (plugin.sugarBoolean) {
                log.info("Sugar Control is enabled!");
            } else {
                log.info("Sugar control is disabled!");
            }
        }
        plugin.cactusBoolean = config.getBoolean("Enabled.Cactus");
        if (debug) {
            if (plugin.cactusBoolean) {
                log.info("Cactus control is enabled!");
            } else {
                log.info("Cactus control is disabled!");
            }
        }
        plugin.configVersion = config.getInt("Version");
        for (GrowthLimiterWorld world : Worlds) {
            world.grassGrowth = config.getInt("Worlds." + world.worldName + ".Grass");
            world.myceliumGrowth = config.getInt("Worlds." + world.worldName + ".Mycelium");
            world.vineGrowth = config.getInt("Worlds." + world.worldName + ".Vine");
            world.vineMaxDistance = config.getInt("Worlds." + world.worldName + ".Vine-Distance");
            world.wheatGrowth = config.getInt("Worlds." + world.worldName + ".Wheat");
            world.cactusGrowth = config.getInt("Worlds." + world.worldName + ".Cactus");
            world.sugarCaneGrowth = config.getInt("Worlds." + world.worldName + ".Sugar");
            world.grassGrowthPercent = (double) world.grassGrowth / 100;
            world.myceliumGrowthPercent = (double) world.myceliumGrowth / 100;
            world.vineGrowthPercent = (double) world.vineGrowth / 100;
            world.wheatGrowthPercent = (double) world.wheatGrowth / 100;
            world.cactusGrowthPercent = (double) world.cactusGrowth / 100;
            world.sugarGrowthPercent = (double) world.sugarCaneGrowth / 100;
        }
        plugin.worlds = Worlds;
    }

    /**
     * Called when a legacy config is detected and needs to be converted
     * Effective as of v0.3 (Config version 3)
     */
    private void legacyConfigConverter() {
        // Should already have been parsed, so Worlds already exists;
        switch (configConvertVersion) {
        case 2: // GrowthLimiter v0.2
            log.info("Converting old config file to latest version!");
            config.set("Growth", null);
            config.set("Worlds", null);
            config.set("Debug", null);
            config.set("Version", configVersion);
            config.set("Debug", debug);
            config.createSection("Enabled");
            config.set("Enabled.Grass", true);
            config.set("Enabled.Mycelium", true);
            config.set("Enabled.Vine", true);
            config.set("Enabled.Wheat", true);
            // Create per world configuration
            log.info("Creating defaults for each world from old config data");
            for (GrowthLimiterWorld world : Worlds) {
                // Create the world section for this world
                config.createSection("Worlds." + world.worldName);

                // Local variable to make writting easier
                ConfigurationSection worldSection = config.getConfigurationSection("Worlds." + world.worldName);

                // Set the stored values from legacyConfigParser();
                worldSection.set("Grass", world.grassGrowth);
                worldSection.set("Mycelium", world.myceliumGrowth);
                worldSection.set("Vine", world.vineGrowth);
                worldSection.set("Vine-Distance", world.vineMaxDistance);
                worldSection.set("Wheat", world.wheatGrowth);
            }
            plugin.saveConfig();
            log.info("Done converting! Please make sure to check out the config for new things!");
            break;
        case 3: // GrowthLimiter v0.3
            log.info("Converting from Version 3 to Current Version!");
            config.set("Version", configVersion);
            // Add Wheat stuffs
            config.set("Enabled.Wheat", true);
            for (GrowthLimiterWorld world : Worlds) {
                ConfigurationSection worldSection = config.getConfigurationSection("Worlds." + world.worldName);
                worldSection.set("Wheat", world.wheatGrowth);
            }
            plugin.saveConfig();
            log.info("Done converting! Please make sure to check out the config for new things!");
            break;

        }

    }

    private void listWorldConfigs() {
        if (!firstTimeLoading) {
            for (GrowthLimiterWorld world : Worlds) {
                log.info(" " + world.worldName + " is being loaded!");
                if (plugin.grassBoolean) {
                    log.info(" " + world.worldName + "'s grass growth control is ENABLED");
                    log.info(" " + world.worldName + "'s grass growth percentage is : " + world.grassGrowth + "%");
                } else
                    log.info(" " + world.worldName + "'s grass growth control is DISABLED");
                if (plugin.myceliumBoolean) {
                    log.info(" " + world.worldName + "'s mycelium growth control is ENABLED");
                    log.info(" " + world.worldName + "'s mycelium growth percentage is : " + world.myceliumGrowth + "%");
                } else
                    log.info(" " + world.worldName + "'s mycelium growth control is DISABLED");
                if (plugin.vineBoolean) {
                    log.info(" " + world.worldName + "'s vine growth control is ENABLED");
                    log.info(" " + world.worldName + "'s vine growth percentage is : " + world.vineGrowth + "%");
                    log.info(" " + world.worldName + "'s vine maximum length is : " + world.vineMaxDistance + " blocks");
                } else
                    log.info(" " + world.worldName + "'s vine growth control is DISABLED");
                if (plugin.wheatBoolean) {
                    log.info(" " + world.worldName + "'s wheat growth control is ENABLED");
                    log.info(" " + world.worldName + "'s wheat growth percentage is : " + world.wheatGrowthPercent + "%");
                } else
                    log.info(" " + world.worldName + "'s wheat growth control is DISABLED");
            }

        }

    }

    public List<GrowthLimiterWorld> getWorlds() {
        return Worlds;
    }
}
