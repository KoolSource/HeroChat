/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.command.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.command.BaseCommand;

public class RemoveCommand extends BaseCommand {

    public RemoveCommand(HeroChat plugin) {
        super(plugin);
        name = "Remove";
        description = "Removes a command";
        usage = "§e/ch remove §9<channel>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("ch remove");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ChannelManager cm = plugin.getChannelManager();
            Channel c = cm.getChannel(args[0]);
            if (c != null) {
                if (plugin.getPermissionManager().isAdmin(player) || c.getModerators().contains(player.getName())) {
                    if (cm.getChannels().size() > 1) {
                        if (!c.equals(cm.getDefaultChannel())) {
                            String[] players = cm.getPlayerList();
                            for (String s : players) {
                                if (cm.getActiveChannel(s).equals(c)) {
                                    List<Channel> joined = cm.getJoinedChannels(s);
                                    cm.setActiveChannel(s, joined.get(0).getName());
                                    Player p = plugin.getServer().getPlayer(s);
                                    if (p != null) {
                                        p.sendMessage(plugin.getTag() + "§cSet active channel to " + cm.getActiveChannel(s).getCName());
                                    }
                                }
                            }
                            cm.removeChannel(c);
                            sender.sendMessage(plugin.getTag() + "§cChannel " + c.getCName() + " §cremoved");
                            try {
                                plugin.getConfigManager().save();
                            } catch (Exception e) {
                                plugin.log(Level.WARNING, "Error encountered while saving data. Disabling HeroChat.");
                                plugin.getServer().getPluginManager().disablePlugin(plugin);
                                return;
                            }
                        } else {
                            sender.sendMessage(plugin.getTag() + "§cYou cannot delete the default channel");
                        }
                    } else {
                        sender.sendMessage(plugin.getTag() + "§cYou cannot delete the last channel");
                    }
                } else {
                    sender.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cChannel not found");
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
        }
    }
}
