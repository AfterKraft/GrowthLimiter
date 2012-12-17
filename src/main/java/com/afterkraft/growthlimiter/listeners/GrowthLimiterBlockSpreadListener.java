package com.afterkraft.growthlimiter.listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.afterkraft.growthlimiter.GrowthLimiter;
import com.afterkraft.growthlimiter.GrowthLimiterConfig;
import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class GrowthLimiterBlockSpreadListener implements Listener {
    
    
    private GrowthLimiter plugin;
    private final Logger log;
    private double grassGrowthPercent = 0.5;
    private double myceliumGrowthPercent = 0.5;
    private double vineGrowthPercent = 0.5;
    private List<GrowthLimiterWorld> Worlds;
    
    public GrowthLimiterBlockSpreadListener(GrowthLimiter plugin, GrowthLimiterConfig config) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.Worlds = config.getWorlds();
        this.log = plugin.getLogger();
        
    }
    /**
     * EventHandler for Grass BlockSpreadEvent.
     * 
     * This should fire for the following three:
     * 
     * Grass, Mycelium, Vines
     * 
     * @param BlockSpreadEvents
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockSpread(BlockSpreadEvent event) {
        int fromType = event.getSource().getTypeId(); // Get source ID
        String eventWorldString = event.getSource().getWorld().getName();
        for (GrowthLimiterWorld world : Worlds) {
            if (eventWorldString.equalsIgnoreCase(world.worldName)) {
                // Grass Processing
                if (fromType == 2) {
                    if (plugin.grassBoolean) {
                        Random rand = new Random();
                        double random = rand.nextDouble();

                        if (random <= grassGrowthPercent) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info(" The growth percent is :" + world.grassGrowth + "%");
                                log.info(" With a random number of " + random + ", nothing happened!");
                            }

                            return;
                        } else {
                            if (plugin.debug) {
                                log.info(" The growth percent is :" + world.grassGrowth + "%");
                                log.info(" With a random number of " + random + ", Grass grew!");
                            }
                            return;
                        }
                    }

                }
                // Now for Mycelium processing
                if (fromType == 110) {
                    if (plugin.mycliumBoolean) {
                        Random rand = new Random();
                        double random = rand.nextDouble();

                        if (random <= myceliumGrowthPercent) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info(" The growth percent is :" + world.myceliumGrowth + "%");
                                log.info(" " + eventWorldString + "With a random number of " + random
                                        + ", nothing happened!");
                            }

                            return;
                        } else {
                            if (plugin.debug) {
                                log.info(" The growth percent is :" + world.myceliumGrowth + "%");
                                log.info(" " + eventWorldString + " With a random number of " + random
                                        + ", Mycelium grew!");
                            }
                            return;
                        }
                    }

                }
                // Vine Processing
                if (fromType == 106) {
                    if (plugin.vineBoolean) {
                        Random rand = new Random();
                        double random = rand.nextDouble();
                        Block block = event.getBlock().getRelative(0, world.vineMaxDistance, 0);
                        if ((random <= vineGrowthPercent) || (block.getTypeId() == 106)) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info(" The growth percent is :" + world.vineGrowth + "%");
                                log.info(" With a random number of " + random + ", nothing happened!");
                            }

                            return;
                        } else {
                            if (plugin.debug) {
                                log.info(" The growth percent is :" + world.vineGrowth + "%");
                                log.info(" With a random number of " + random + ", Grass grew!");
                            }
                            return;
                        }
                    }
                }

            } else {
                if (plugin.debug) {
                    log.info(" The World: " + eventWorldString + ", is not enabled!");
                    log.info(" " + eventWorldString + ": Growth is normal!");
                }
            }

        }
    }

}
