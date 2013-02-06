package com.afterkraft.growthlimiter.command;

import org.bukkit.entity.Player;

public class GrowthLimiterCmdModify {

    public void modifyCmd(Player player, String[] args) {
        if (args.length !=3) {
            player.sendMessage("Nope, please do /growth modify for info on to use this command!");
        }
        
    }

}
