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
        } else {
            try {
                config.load(configFile);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Determine whether the configuration file needs to be
                // converted or not
                if (config.contains("Version") && config.getInt("Version") == configVersion) {
                    configConvertVersion = configVersion;
                    // Parse config now
                    growthConfigParser();
                } else {
                    log.info(" Old config detected! Reading old config to prepare for conversion!");
                    legacyConfigParser();
                    log.info(" Converting old config! Please look over config for new items!");
                    configConvertVersion = 2;
                    legacyConfigConverter();
                }
                log.info(" The following worlds are configured: " + Worlds);
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
        stringWorlds = config.getStringList("Worlds");
        switch (configConvertVersion) {
        case 1: // Parse for GrowthLimiter v0.1
            break;
        case 2: // Parse for GrowthLimiter v0.2
            log.info("Detecting GrowthLimiter v0.2!, Parsing!");
            debug = config.getBoolean("Debug");
            for (String worldString : stringWorlds) {
                GrowthLimiterWorld world = new GrowthLimiterWorld();
                // Retrieve the World Name
                world.worldName = worldString;
                // Default previously non-existant boolean
                world.grassBoolean = true;
                // Retrieve Global Growth Value
                world.grassGrowth = config.getInt("Growth.Grass");
                // Default previously non-existant boolean
                world.mycliumBoolean = true;
                // Retrieve Mycelium Growth Value
                world.myceliumGrowth = config.getInt("Growth.Mycelium");
                // Default previously non-existant boolean
                world.vineBoolean = false;
                // Default previously non-existant int
                world.vineGrowth = 50;
                // Default previously non-existant boolean
                world.vineMaxDistance = 10;
            }
            break;
        }

    }

    /**
     * Parse for current config if passed not-legacy-check Effective as of v0.3
     * (Config version 3)
     */
    private void growthConfigParser() {
        stringWorlds = new ArrayList<String>();
        stringWorlds.addAll(plugin.getConfig().getConfigurationSection("Worlds").getKeys(false));
        for (String stringWorldName : stringWorlds) {
            GrowthLimiterWorld temp = new GrowthLimiterWorld();
            temp.worldName = stringWorldName;
            Worlds.add(temp);
        }
        plugin.debug = config.getBoolean("Debug");
        plugin.grassBoolean = config.getBoolean("Enabled.Grass");
        plugin.mycliumBoolean = config.getBoolean("Enabled.Mycelium");
        plugin.vineBoolean = config.getBoolean("Enabled.Vine");
        plugin.configVersion = config.getInt("Version");
        for (GrowthLimiterWorld world : Worlds) {
            world.grassGrowth = config.getInt("Worlds." + world.worldName + "Grass-Growth");
            world.myceliumGrowth = config.getInt("Worlds." + world.worldName + "Mycelium-Growth");
            world.vineGrowth = config.getInt("Worlds." + world.worldName + "Vine-Growth");
            world.vineMaxDistance = config.getInt("Worlds." + world.worldName + "Vine-Distance");
        }
        plugin.worlds = Worlds;
    }

    /**
     * Called when a legacy config is detected and needs to be converted
     * Effective as of v0.3 (Config version 3)
     */
    private void legacyConfigConverter() {
        if (configConvertVersion == 2) {
            log.info("Converting old config file to latest version!");
            config.set("Growth", null);
            config.set("Worlds", null);
            config.set("Debug", null);
            config.set("Version", 3);
            config.set("Debug", false);
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
                if (!(plugin.getServer().getWorld(world.worldName) == null)) {
                    log.info(" " + world.worldName + " is being loaded!");
                    if (world.grassBoolean) {
                        log.info(" " + world.worldName + "'s grass growth control is ENABLED");
                        log.info(" " + world.worldName + "'s grass growth percentage is :" + world.grassGrowth + "%");
                    } else
                        log.info(" " + world.worldName + "'s grass growth control is DISABLED");
                    if (world.mycliumBoolean) {
                        log.info(" " + world.worldName + "'s mycelium growth control is ENABLED");
                        log.info(" " + world.worldName + "'s mycelium growth percentage is :" + world.myceliumGrowth
                                + "%");
                    } else
                        log.info(" " + world.worldName + "'s mycelium growth control is DISABLED");
                    if (world.vineBoolean) {
                        log.info(" " + world.worldName + "'s vine growth control is ENABLED");
                        log.info(" " + world.worldName + "'s vine growth percentage is :" + world.vineGrowth + "%");
                        log.info(" " + world.worldName + "'s vine maximum length is :" + world.vineMaxDistance
                                + " blocks");
                    } else
                        log.info(" " + world.worldName + "'s vine growth control is DISABLED");
                }
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
