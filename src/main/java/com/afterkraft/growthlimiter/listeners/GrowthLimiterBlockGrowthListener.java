package com.afterkraft.growthlimiter.listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import com.afterkraft.growthlimiter.GrowthLimiter;
import com.afterkraft.growthlimiter.GrowthLimiterConfig;
import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;

public class GrowthLimiterBlockGrowthListener implements Listener {

    private GrowthLimiter plugin;
    private final Logger log;
    private List<GrowthLimiterWorld> Worlds;

    public GrowthLimiterBlockGrowthListener(GrowthLimiter plugin, GrowthLimiterConfig config) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.Worlds = config.getWorlds();
        this.log = plugin.getLogger();

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockGrowth(BlockGrowEvent event) {
        Material fromType = event.getBlock().getType();
        String eventWorldString = event.getBlock().getWorld().getName();
        for (GrowthLimiterWorld world : Worlds) {
            if (eventWorldString.equalsIgnoreCase(world.worldName)) {
                Random rand = new Random();
                // Wheat Processing
                if (fromType == Material.CROPS) {
                    double random = rand.nextDouble();
                    if (random <= world.wheatGrowthPercent) {
                        event.setCancelled(true);
                        if (plugin.debug) {
                            log.info("[" + world.worldName + "]" + " The wheat growth percent is: " + world.wheatGrowth
                                    + "%");

                            log.info("[" + world.worldName + "]" + " With a random number of " + random
                                    + ", nothing happened!");
                        }
                        return;
                    } else {
                        if (plugin.debug) {
                            log.info("[" + world.worldName + "]" + " The growth percent for Wheat growth is :"
                                    + world.wheatGrowth + "%");
                            log.info("[" + world.worldName + "]" + " With a random number of " + random
                                    + ", Wheat grew!");
                        }
                    }
                } else if (fromType == Material.CACTUS) {
                    double random = rand.nextDouble();
                    if (random <= world.cactusGrowthPercent) {
                        event.setCancelled(true);
                        if (plugin.debug) {
                            log.info("[" + world.worldName + "]" + " The cactus growth percent is: " + world.cactusGrowth
                                     + "%");

                            log.info("[" + world.worldName + "]" + " With a random number of " + random
                                     + ", nothing happened!");
                        }
                    }
                } else if (fromType == Material.SUGAR_CANE_BLOCK) {
                    double random = rand.nextDouble();
                    if (random <= world.sugarGrowthPercent) {
                        event.setCancelled(true);
                        if (plugin.debug) {
                            log.info("[" + world.worldName + "]" + " The sugarcane growth percent is: " + world.sugarCaneGrowth
                                     + "%");

                            log.info("[" + world.worldName + "]" + " With a random number of " + random
                                     + ", nothing happened!");
                        }
                    }
                }
            }
        }
    }

}
