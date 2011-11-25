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
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.command.BaseCommand;

public class IgnoreCommand extends BaseCommand {

    public IgnoreCommand(HeroChat plugin) {
        super(plugin);
        name = "Ignore";
        description = "Ignores all messages from a player";
        usage = "§e/ch ignore §8[player] §eOR /ignore §8[player]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("ch ignore");
        identifiers.add("ignore");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player ignorer = (Player) sender;
            if (args.length == 0) {
                displayIgnoreList(ignorer, plugin.getChannelManager().getIgnoreList(ignorer.getName()));
            } else {
                Player ignoree = plugin.getServer().getPlayer(args[0]);
                if (ignoree != null) {
                    toggleIgnore(ignorer, ignoree);
                } else {
                    sender.sendMessage(plugin.getTag() + "§cPlayer not found");
                }
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
        }
    }

    private void displayIgnoreList(Player player, List<String> ignoreList) {
        String ignoreListMsg;
        if (ignoreList.isEmpty()) {
            ignoreListMsg = plugin.getTag() + "§cCurrently ignoring no one.";
        } else {
            ignoreListMsg = "Currently ignoring: ";
            for (String s : ignoreList) {
                ignoreListMsg += s + ",";
            }
            ignoreListMsg = ignoreListMsg.substring(0, ignoreListMsg.length() - 1);
        }
        player.sendMessage(ignoreListMsg);
    }

    private void toggleIgnore(Player ignorer, Player ignoree) {
        if (plugin.getPermissionManager().isAdmin(ignoree)) {
            ignorer.sendMessage(plugin.getTag() + "§cYou can't ignore admins");
            return;
        }

        if (ignorer.getName().equals(ignoree.getName())) {
            ignorer.sendMessage(plugin.getTag() + "§cYou cannot ignore yourself");
            return;
        }

        ChannelManager cm = plugin.getChannelManager();
        if (cm.isIgnoring(ignorer.getName(), ignoree.getName())) {
            cm.removeIgnore(ignorer.getName(), ignoree.getName());
            ignorer.sendMessage(plugin.getTag() + "§cNo longer ignoring " + ignoree.getName());
        } else {
            cm.addIgnore(ignorer.getName(), ignoree.getName());
            ignorer.sendMessage(plugin.getTag() + "§cNow ignoring " + ignoree.getName());
        }
    }
}
