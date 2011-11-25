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
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.command.BaseCommand;

public class LeaveCommand extends BaseCommand {

    public LeaveCommand(HeroChat plugin) {
        super(plugin);
        name = "Leave";
        description = "Leaves a channel";
        usage = "§e/ch leave §9<channel> §eOR /leave §9<channel>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("ch leave");
        identifiers.add("leave");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player joiner = (Player) sender;
            String name = joiner.getName();
            ChannelManager cm = plugin.getChannelManager();
            Channel c = cm.getChannel(args[0]);
            if (c != null) {
                if (c.getPlayers().contains(name)) {
                    if (!c.isForced()) {
                        c.removePlayer(name);
                        sender.sendMessage(plugin.getTag() + "§cLeft channel " + c.getCName());
                        if (cm.getActiveChannel(name).equals(c)) {
                            List<Channel> joined = cm.getJoinedChannels(name);
                            cm.setActiveChannel(name, joined.get(0).getName());
                            sender.sendMessage(plugin.getTag() + "§cSet active channel to " + cm.getActiveChannel(name).getCName());
                        }
                    } else {
                        sender.sendMessage(plugin.getTag() + "§cYou cannot leave " + c.getCName());
                    }
                } else {
                    sender.sendMessage(plugin.getTag() + "§cYou are not in " + c.getCName());
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cChannel not found");
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
        }
    }
}
