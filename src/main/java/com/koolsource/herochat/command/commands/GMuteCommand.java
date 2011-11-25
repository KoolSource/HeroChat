package com.koolsource.herochat.command.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.command.BaseCommand;

public class GMuteCommand extends BaseCommand {

    public GMuteCommand(HeroChat plugin) {
        super(plugin);
        name = "Global Mute";
        description = "Prevents a player from speaking in any channel";
        usage = "§e/ch gmute §8[player] §eOR /gmute §8[player]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("ch gmute");
        identifiers.add("gmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager cm = plugin.getChannelManager();
        if (args.length == 0) {
            displayMuteList(sender);
        } else {
            if (sender instanceof Player) {
                Player muter = (Player) sender;
                if (plugin.getPermissionManager().isAdmin(muter)) {
                    Player mutee = plugin.getServer().getPlayer(args[0]);
                    if (mutee != null) {
                        String name = mutee.getName();
                        if (!(plugin.getPermissionManager().isAdmin(mutee))) {
                            if (cm.getMutelist().contains(name)) {
                                cm.getMutelist().remove(name);
                                muter.sendMessage(plugin.getTag() + "§c" + name + " has been globally unmuted");
                                mutee.sendMessage(plugin.getTag() + "§cYou have been globally unmuted");
                            } else {
                                cm.getMutelist().add(name);
                                muter.sendMessage(plugin.getTag() + "§c" + name + " has been globally muted");
                                mutee.sendMessage(plugin.getTag() + "§cYou have been globally muted");
                            }
                        } else {
                            muter.sendMessage(plugin.getTag() + "§cYou cannot globally mute " + name);
                        }
                    } else {
                        muter.sendMessage(plugin.getTag() + "§cPlayer not found");
                    }
                } else {
                    muter.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
            }
        }
    }

    private void displayMuteList(CommandSender sender) {
        String muteListMsg;
        List<String> mutes = plugin.getChannelManager().getMutelist();
        if (mutes.isEmpty()) {
            muteListMsg = plugin.getTag() + "§cNo one is currently muted";
        } else {
            muteListMsg = "Currently muted: ";
            for (String s : mutes) {
                muteListMsg += s + ",";
            }
            muteListMsg = muteListMsg.substring(0, muteListMsg.length() - 1);
        }
        sender.sendMessage(muteListMsg);
    }
}
