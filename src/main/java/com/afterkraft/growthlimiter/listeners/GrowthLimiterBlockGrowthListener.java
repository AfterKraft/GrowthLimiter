package com.afterkraft.growthlimiter.listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

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
        int fromType = event.getBlock().getTypeId();
        String eventWorldString = event.getBlock().getWorld().getName();
        for (GrowthLimiterWorld world : Worlds) {
            if (eventWorldString.equalsIgnoreCase(world.worldName)) {
                // Wheat Processing
                if (fromType == 59) {
                    Random rand = new Random();
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
                }
            }
        }
    }

}
