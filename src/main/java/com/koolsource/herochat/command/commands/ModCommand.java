/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.command.BaseCommand;

public class ModCommand extends BaseCommand {

    public ModCommand(HeroChat plugin) {
        super(plugin);
        name = "Mod";
        description = "Grants moderator privileges to a player";
        usage = "§e/ch mod §9<channel> <player>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("ch mod");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String name = player.getName();
            Channel c = plugin.getChannelManager().getChannel(args[0]);
            if (c != null) {
                if (c.getModerators().contains(name) || plugin.getPermissionManager().isAdmin(player)) {
                    Player mod = plugin.getServer().getPlayer(args[1]);
                    if (mod != null) {
                        if (!c.getModerators().contains(mod.getName())) {
                            c.getModerators().add(mod.getName());
                            sender.sendMessage(plugin.getTag() + "§c" + mod.getName() + " is now moderating " + c.getCName());
                            mod.sendMessage(plugin.getTag() + "§cYou are now moderating " + c.getCName());
                        } else {
                            sender.sendMessage(plugin.getTag() + "§c" + mod.getName() + " is already moderating " + c.getCName());
                        }
                    } else {
                        sender.sendMessage(plugin.getTag() + "§cPlayer not found");
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
