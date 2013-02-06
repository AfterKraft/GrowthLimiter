package com.afterkraft.growthlimiter.command;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.afterkraft.growthlimiter.GrowthLimiter;
import com.afterkraft.growthlimiter.GrowthLimiterConfig;
import com.afterkraft.growthlimiter.api.GrowthLimiterWorld;

public class GrowthLimiterCommander implements CommandExecutor {

    protected GrowthLimiter plugin;
    protected Logger log;
    protected GrowthLimiterConfig config;
    protected List<GrowthLimiterWorld> Worlds;
    private GrowthLimiterCmdHelp helpCmd = new GrowthLimiterCmdHelp();
    private GrowthLimiterCmdInfo infoCmd = new GrowthLimiterCmdInfo();
    private GrowthLimiterCmdModify modifyCmd = new GrowthLimiterCmdModify();
    private GrowthLimiterCmdCreate createCmd = new GrowthLimiterCmdCreate();
    private GrowthLimiterCmdRemove removeCmd = new GrowthLimiterCmdRemove();

    public GrowthLimiterCommander(GrowthLimiter plugin, GrowthLimiterConfig config) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        this.config = config;
        Worlds = config.getWorlds();

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { // Process for console side commands
            sender.sendMessage("Can not do anything by console! Sorry!");
            return false;
        } else {
            Player player = (Player) sender;
            if (player.hasPermission("growthlimiter.admin")) {
                GrowthLimiterWorld world = new GrowthLimiterWorld();
                world.worldName = player.getWorld().getName();
                if (isKraftWorld(world)) {
                    if (args.length == 0) {
                        helpCmd.showHelp(player);
                    } else if (args[0].toString().equalsIgnoreCase("create")) {
                        modifyCmd.modifyCmd(player, args);
                    } else if (args[0].toString().equalsIgnoreCase("remove")) {
                        removeCmd.removeCmd(player, args);
                    } else if (args[0].toString().equalsIgnoreCase("info")) {
                        infoCmd.infoCmd(player, args);
                    } else if (args[0].toString().equalsIgnoreCase("help")) {
                        helpCmd.helpCmd(player, args);
                    } else {
                        helpCmd.showCmdHelp(player, args);
                    }
                } else {
                    player.sendMessage("GrowthLimiter isn't enabled in the world: " + world.worldName);
                }
            }
        }
        return false;
    }

    private boolean isKraftWorld(GrowthLimiterWorld world) {
        boolean isKraftWorld = false;
        for (GrowthLimiterWorld worlds : Worlds) {
            if (worlds.worldName.equalsIgnoreCase(world.worldName)) {
                isKraftWorld = true;
            } else {
                isKraftWorld = false;
            }
        }
        return isKraftWorld;
    }
}
