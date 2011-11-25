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

public class BanCommand extends BaseCommand {

    public BanCommand(HeroChat plugin) {
        super(plugin);
        name = "Ban";
        description = "Bans a player from a channel";
        usage = "§e/ch ban §9<channel> <player>";
        minArgs = 1;
        maxArgs = 2;
        identifiers.add("ch ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager cm = plugin.getChannelManager();
        Channel channel = cm.getChannel(args[0]);
        if (channel != null) {
            if (args.length == 1) {
                displayBanList(sender, channel);
            } else {
                if (sender instanceof Player) {
                    Player banner = (Player) sender;
                    if (plugin.getPermissionManager().isAdmin(banner) || channel.getModerators().contains(banner.getName())) {
                        Player banee = plugin.getServer().getPlayer(args[1]);
                        if (banee != null) {
                            String name = banee.getName();
                            if (!(plugin.getPermissionManager().isAdmin(banee) || channel.getModerators().contains(name))) {
                                if (channel.getBlacklist().contains(name)) {
                                    channel.getBlacklist().remove(name);
                                    banner.sendMessage(plugin.getTag() + name + "§c has been unbanned from " + channel.getCName());
                                    banee.sendMessage(plugin.getTag() + "§cYou have been unbanned from " + channel.getCName());
                                } else {
                                    channel.getBlacklist().add(name);
                                    channel.removePlayer(name);
                                    banner.sendMessage(plugin.getTag() + name + "§c has been banned from " + channel.getCName());
                                    banee.sendMessage(plugin.getTag() + "§cYou have been banned from " + channel.getCName());
                                    if (cm.getActiveChannel(name).equals(channel)) {
                                        List<Channel> joined = cm.getJoinedChannels(name);
                                        cm.setActiveChannel(name, joined.get(0).getName());
                                        banee.sendMessage(plugin.getTag() + "§cSet active channel to " + cm.getActiveChannel(name).getCName());
                                    }
                                }
                            } else {
                                banner.sendMessage(plugin.getTag() + "§cYou cannot ban " + name + " from " + channel.getCName());
                            }
                        } else {
                            banner.sendMessage(plugin.getTag() + "§cPlayer not found");
                        }
                    } else {
                        banner.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                    }
                } else {
                    sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
                }
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cChannel not found");
        }
    }

    private void displayBanList(CommandSender sender, Channel channel) {
        String banListMsg;
        List<String> bans = channel.getBlacklist();
        if (bans.isEmpty()) {
            banListMsg = plugin.getTag() + "§cNo one is currently banned from " + channel.getCName();
        } else {
            banListMsg = "Currently banned from " + channel.getCName() + "§f: ";
            for (String s : bans) {
                banListMsg += s + ",";
            }
            banListMsg = banListMsg.substring(0, banListMsg.length() - 1);
        }
        sender.sendMessage(banListMsg);
    }
}
