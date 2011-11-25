/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.command.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.command.BaseCommand;

public class WhoCommand extends BaseCommand {

    public WhoCommand(HeroChat plugin) {
        super(plugin);
        name = "Who";
        description = "Lists all users in your active channel";
        usage = "§e/ch who";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("ch who");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String name = player.getName();
            Channel c = plugin.getChannelManager().getActiveChannel(name);
            if (c != null) {
                List<String> players = c.getPlayers();
                String playerList = "§cCurrently in " + c.getCName() + "§f: ";
                for (String pName : players) {
                    Player p = plugin.getServer().getPlayer(pName);
                    if (p != null) {
                        if (plugin.getPermissionManager().isAdmin(p)) {
                            pName = "@" + pName;
                        } else if (c.getModerators().contains(pName)) {
                            pName += "*";
                        }
                        pName += ", ";
                        playerList += pName;
                    }
                }
                playerList = playerList.substring(0, playerList.length() - 2);
                sender.sendMessage(playerList);
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
        }
    }
}
