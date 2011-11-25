package com.koolsource.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.command.BaseCommand;

public class ToggleCommand extends BaseCommand {

    private boolean allEnabled = true;

    public ToggleCommand(HeroChat plugin) {
        super(plugin);
        name = "Toggle";
        description = "Temporarily enables or disables a channel";
        usage = "§e/ch toggle §8[channel]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("ch toggle");
        notes.add("§cNote: §eIf no channel is provided, all channels are toggled");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager cm = plugin.getChannelManager();
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player muter = (Player) sender;
                if (muter == null || plugin.getPermissionManager().isAdmin(muter)) {
                    if (allEnabled) {
                        for (Channel c : cm.getChannels()) {
                            c.setEnabled(false);
                        }
                        allEnabled = false;
                        sender.sendMessage(plugin.getTag() + "§cDisabled all channels");
                    } else {
                        for (Channel c : cm.getChannels()) {
                            c.setEnabled(true);
                        }
                        allEnabled = true;
                        sender.sendMessage(plugin.getTag() + "§cEnabled all channels");
                    }
                } else {
                    muter.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
            }
        } else {
            Channel channel = cm.getChannel(args[0]);
            if (channel != null) {
                if (sender instanceof Player) {
                    Player muter = (Player) sender;
                    if (muter == null || plugin.getPermissionManager().isAdmin(muter) || channel.getModerators().contains(muter.getName())) {
                        if (channel.isEnabled()) {
                            channel.setEnabled(false);
                            sender.sendMessage(plugin.getTag() + "§cDisabled " + channel.getCName());
                        } else {
                            channel.setEnabled(true);
                            sender.sendMessage(plugin.getTag() + "§cEnabled " + channel.getCName());
                        }
                    } else {
                        muter.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                    }
                } else {
                    sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cChannel not found");
            }
        }
    }
}
