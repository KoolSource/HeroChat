/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.command.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.HeroChat.ChatColor;
import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.command.BaseCommand;

public class CreateCommand extends BaseCommand {

    private static final String[] RESERVED_NAMES = {"ch", "join", "leave", "ignore", "help", "ban", "create", "kick", "list", "mod", "qm", "reload", "remove",
        "who"};

    public CreateCommand(HeroChat plugin) {
        super(plugin);
        name = "Create";
        description = "Creates a channel";
        usage = "§e/ch create §9<name> <nick> §8[p:pass] [color:#] [-options]";
        minArgs = 2;
        maxArgs = 5;
        identifiers.add("ch create");
        notes.add("§cOptions (combinable, ie. -hsqf):");
        notes.add("-h   Hidden from /ch channels list");
        notes.add("-j   Show join and leave messages");
        notes.add("§cAdmin-only options:");
        notes.add("-a   Automatically joined by new users");
        notes.add("-q   Allow quick message shortcut");
        notes.add("-f   Force users to stay in this channel");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager cm = plugin.getChannelManager();
        if (sender instanceof Player) {
            Player creator = (Player) sender;
            if (plugin.getPermissionManager().canCreate(creator)) {
                for (String reserved : RESERVED_NAMES) {
                    if (args[0].equalsIgnoreCase(reserved)) {
                        sender.sendMessage(plugin.getTag() + "§cThat name is reserved");
                        return;
                    } else if (args[1].equalsIgnoreCase(reserved)) {
                        sender.sendMessage(plugin.getTag() + "§cThat nick is reserved");
                        return;
                    }
                }
                if (cm.getChannel(args[0]) != null) {
                    sender.sendMessage(plugin.getTag() + "§cThat name is taken");
                    return;
                } else if (cm.getChannel(args[1]) != null) {
                    sender.sendMessage(plugin.getTag() + "§cThat nick is taken");
                    return;
                }
                Channel c = createChannel(args, plugin.getPermissionManager().isAdmin(creator));
                if (c != null) {
                    String name = creator.getName();
                    c.getModerators().add(name);
                    c.addPlayer(name);
                    cm.addChannel(c);
                    cm.setActiveChannel(name, c.getName());
                    sender.sendMessage(plugin.getTag() + "§cCreated channel " + c.getCName());
                    try {
                        plugin.getConfigManager().save();
                    } catch (Exception e) {
                        plugin.log(Level.WARNING, "Error encountered while saving data. Disabling HeroChat.");
                        plugin.getServer().getPluginManager().disablePlugin(plugin);
                        return;
                    }
                } else {
                    sender.sendMessage(plugin.getTag() + "§cInvalid syntax. Type /ch create ? for info");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cYou cannot create channels");
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cYou must be a player to create channels");
        }
    }

    private Channel createChannel(String[] args, boolean full) {
        Channel c = new Channel(plugin);
        c.setName(args[0]);
        c.setNick(args[1]);
        c.setMsgFormat("{default}");
        for (int i = 2; i < args.length; i++) {
            String tmp = args[i].toLowerCase();

            if (tmp.startsWith("p:")) {
                c.setPassword(tmp.substring(2));
            } else if (tmp.startsWith("color:")) {
                try {
                    int color = Integer.parseInt(tmp.substring(6), 16);
                    c.setColor(ChatColor.values()[color]);
                } catch (NumberFormatException e) {
                    return null;
                }
            } else if (tmp.startsWith("-")) {
                tmp = tmp.substring(1);
                applyOptions(c, tmp.toCharArray(), full);
            }
        }
        return c;
    }

    private void applyOptions(Channel c, char[] args, boolean full) {
        for (char option : args) {
            switch (option) {
                case 'h':
                    c.setHidden(true);
                    break;
                case 'j':
                    c.setVerbose(true);
                    break;
                case 'a':
                    if (full) {
                        c.setAutoJoined(true);
                    }
                    break;
                case 'q':
                    if (full) {
                        c.setQuickMessagable(true);
                    }
                    break;
                case 'f':
                    if (full) {
                        c.setForced(true);
                    }
                    break;
            }
        }
    }
}
