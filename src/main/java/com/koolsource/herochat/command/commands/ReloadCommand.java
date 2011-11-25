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
import com.koolsource.herochat.command.BaseCommand;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand(HeroChat plugin) {
        super(plugin);
        name = "Reload";
        description = "Reloads the plugin";
        usage = "§e/ch reload";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("ch reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean hasPermission = true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getPermissionManager().isAdmin(player)) {
                sender.sendMessage(plugin.getTag() + "§cPlugin reloaded");
            } else {
                sender.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                hasPermission = false;
            }
        }
        if (hasPermission) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.getConfigManager().savePlayer(player.getName());
            }
            plugin.onEnable();
        }
    }
}
