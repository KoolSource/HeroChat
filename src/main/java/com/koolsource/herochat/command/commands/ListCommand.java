/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.command.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.command.BaseCommand;

public class ListCommand extends BaseCommand {

    private static final int CHANNELS_PER_PAGE = 9;

    public ListCommand(HeroChat plugin) {
        super(plugin);
        name = "List";
        description = "Lists all publicly available channels";
        usage = "§e/ch list §8[page#]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("ch list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String name;
        if (sender instanceof Player) {
            name = ((Player) sender).getName();
        } else {
            name = "";
        }
        List<Channel> visible = getVisibleChannels(plugin.getChannelManager().getChannels(), name);
        int pages = (int) Math.ceil((double) visible.size() / CHANNELS_PER_PAGE);
        int p;
        if (args.length == 0) {
            p = 1;
        } else {
            try {
                p = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + usage);
                return;
            }
        }
        if (p > pages) {
            p = pages;
        }

        sender.sendMessage("§c-----[ " + "§f" + "Channel List <" + p + "/" + pages + ">§c ]-----");
        for (int i = 0; i < CHANNELS_PER_PAGE; i++) {
            int index = (p - 1) * CHANNELS_PER_PAGE + i;
            if (index >= visible.size()) {
                break;
            }
            Channel c = visible.get(index);
            String msg = "  " + c.getColor().str + "[" + c.getNick() + "] " + c.getName();
            if (c.getPlayers().contains(name)) {
                msg = msg.concat(" *");
            }
            sender.sendMessage(msg);
        }
    }

    private List<Channel> getVisibleChannels(List<Channel> channels, String name) {
        List<Channel> visible = new ArrayList<Channel>();
        for (Channel c : channels) {
            if (!c.isHidden() || c.getPlayers().contains(name)) {
                visible.add(c);
            }
        }
        return visible;
    }
}
