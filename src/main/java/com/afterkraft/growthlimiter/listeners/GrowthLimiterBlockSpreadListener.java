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

package com.afterkraft.growthlimiter.listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.afterkraft.growthlimiter.GrowthLimiter;
import com.afterkraft.growthlimiter.GrowthLimiterConfig;
import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class GrowthLimiterBlockSpreadListener implements Listener {

    private GrowthLimiter plugin;
    private final Logger log;
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
     * @param event The event to process
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockSpread(BlockSpreadEvent event) {
        Material fromType = event.getSource().getType(); // Get source ID
        String eventWorldString = event.getSource().getWorld().getName();
        for (GrowthLimiterWorld world : Worlds) {
            if (eventWorldString.equalsIgnoreCase(world.worldName)) {
                // Grass Processing
                if (fromType == Material.GRASS) {
                    if (plugin.grassBoolean) {
                        Random rand = new Random();
                        double random = rand.nextDouble();

                        if (random <= world.grassGrowthPercent) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info("[" + world.worldName + "]" + " The growth percent for Grass growth is :"
                                        + world.grassGrowth + "%");
                                log.info("[" + world.worldName + "]" + " With a random number of " + random
                                        + ", nothing happened!");
                            }

                            return;
                        } else {
                            if (plugin.debug) {
                                log.info("[" + world.worldName + "]" + " The growth percent for Grass growth is :"
                                        + world.grassGrowth + "%");
                                log.info("[" + world.worldName + "]" + " With a random number of " + random
                                        + ", Grass grew!");
                            }
                            return;
                        }
                    }

                }
                // Now for Mycelium processing
                if (fromType == Material.MYCEL) {
                    if (plugin.myceliumBoolean) {
                        Random rand = new Random();
                        double random = rand.nextDouble();

                        if (random <= world.myceliumGrowthPercent) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info("[" + world.worldName + "]" + " The growth percent for Mycelium growth :"
                                        + world.myceliumGrowth + "%");
                                log.info("[" + world.worldName + "]" + "With a random number of " + random
                                        + ", nothing happened!");
                            }

                            return;
                        } else {
                            if (plugin.debug) {
                                log.info("[" + world.worldName + "]" + "The growth percent for Mycelium growth is :"
                                        + world.myceliumGrowth + "%");
                                log.info("[" + world.worldName + "]" + " With a random number of " + random
                                        + ", Mycelium grew!");
                            }
                            return;
                        }
                    }
                }
                // Vine Processing
                if (fromType == Material.VINE) {
                    if (plugin.vineBoolean) {
                        Random rand = new Random();
                        double random = rand.nextDouble();
                        Block block = event.getBlock().getRelative(0, world.vineMaxDistance, 0);
                        if ((random <= world.vineGrowthPercent)) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info("[" + world.worldName + "]" + "The growth percent for Vine growth is :"
                                        + world.vineGrowth + "%");
                                log.info("[" + world.worldName + "]" + "With a random number of " + random
                                        + ", a vine didn't grow!");
                            }
                            return;
                        } else if (block.getType() == Material.VINE) {
                            event.setCancelled(true);
                            if (plugin.debug) {
                                log.info("[" + world.worldName + "]" + "The vine max distance is :"
                                        + world.vineMaxDistance);
                                log.info("[" + world.worldName + "]" + "A vine didn't grow!");
                            }
                        } else if (plugin.debug) {
                            log.info("[" + world.worldName + "]" + " The growth percent for Vine growth is :"
                                    + world.vineGrowth + "%");
                            log.info("[" + world.worldName + "]" + " With a random number of " + random
                                    + ", a vine grew!");
                        }
                        break;
                    }
                }
            } else if (!eventWorldString.equalsIgnoreCase(world.worldName)) {
                if (plugin.debug) {
                    log.info(" The World: " + eventWorldString + ", is not configured!");
                    log.info(" " + eventWorldString + ": Growth is normal!");
                }
            }
        }
    }
}
