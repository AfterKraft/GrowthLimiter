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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;

public class GrowthLimiterConfig {

    protected static boolean debug = false;
    protected static List<GrowthLimiterWorld> Worlds;
    private static List<String> stringWorlds;
    private static boolean firstTimeLoading = true;
    private File configFile;

    private GrowthLimiter plugin;
    private final Logger log;
    private FileConfiguration config;
    private int configConvertVersion = 0;

    private int configVersion = 3;

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
        File configFile = new File(plugin.getDataFolder().toString() + "/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveDefaultConfig();
            growthConfigParser();
        } else {
            try {
                config.load(configFile);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                firstTimeLoading = false;
                // Determine whether the configuration file needs to be
                // converted or not
                configVersion = 3;
                if (config.getInt("Version") == configVersion) {
                    log.info("Loading Configuration file");
                    configConvertVersion = configVersion;
                    // Parse config now
                    growthConfigParser();
                } else {
                    log.info("Old config detected! Reading old config to prepare for conversion!");
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
        case 3: 
            growthConfigParser();
            break;
        }

    }

    /**
     * Parse for current config if passed not-legacy-check Effective as of v0.3
     * (Config version 3)
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
        plugin.grassBoolean = config.getBoolean("Enabled.Grass");
        if (debug) {
            if (plugin.grassBoolean) {
                log.info("Grass control is enabled!");
            } else {
                log.info("Grass control is disabled!");
            }
        }
        plugin.myceliumBoolean = config.getBoolean("Enabled.Mycelium");
        if (debug) {
            if (plugin.myceliumBoolean) {
                log.info("Mycelium control is enabled!");
            } else {
                log.info("mycelium control is disabled!");
            }
        }
        plugin.vineBoolean = config.getBoolean("Enabled.Vine");
        if (debug) {
            if (plugin.vineBoolean) {
                log.info("Vine control is enabled!");
            } else {
                log.info("Vine control is disabled!");
            }
        }
        plugin.configVersion = config.getInt("Version");
        for (GrowthLimiterWorld world : Worlds) {
            world.grassGrowth = config.getInt("Worlds." + world.worldName + ".Grass");
            world.myceliumGrowth = config.getInt("Worlds." + world.worldName + ".Mycelium");
            world.vineGrowth = config.getInt("Worlds." + world.worldName + ".Vine");
            world.vineMaxDistance = config.getInt("Worlds." + world.worldName + ".Vine-Distance");
            world.grassGrowthPercent = (double) world.grassGrowth/100;
            world.myceliumGrowthPercent = (double) world.myceliumGrowth/100;
            world.vineGrowthPercent = (double) world.vineGrowth/100;
        }
        plugin.worlds = Worlds;
    }

    /**
     * Called when a legacy config is detected and needs to be converted
     * Effective as of v0.3 (Config version 3)
     */
    private void legacyConfigConverter() {
        // Should already have been parsed, so Worlds already exists;
        if (configConvertVersion == 2) {
            log.info("Converting old config file to latest version!");
            config.set("Growth", null);
            config.set("Worlds", null);
            config.set("Debug", null);
            config.set("Version", 3);
            config.set("Debug", debug);
            config.createSection("Enabled");
            config.set("Enabled.Grass", true);
            config.set("Enabled.Mycelium", true);
            config.set("Enabled.Vine", true);
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
            }
            log.info("Done converting! Please make sure to check out the config for new things!");
        }
        try {
            File configFile = new File(plugin.getDataFolder().toString() + "/config.yml");
            config.save(configFile);
        } catch (IOException e) {
            log.warning("Hey, I had issues saving the config file! \n" + e);
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
            }

        }

    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            log.warning("Hey, I couldn't save the config file!!" + e);
        }
    }

    public List<GrowthLimiterWorld> getWorlds() {
        return Worlds;
    }
}
