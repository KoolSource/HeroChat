package com.koolsource.herochat.command.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.channels.ConversationManager;
import com.koolsource.herochat.command.BaseCommand;
import com.koolsource.herochat.util.Messaging;

public class TellCommand extends BaseCommand {

    public TellCommand(HeroChat plugin) {
        super(plugin);
        name = "Tell";
        description = "Starts or ends a private conversation";
        usage = "§e/ch tell §9<player> §8[msg] §eOR /tell §9<player> §8[msg]";
        minArgs = 0;
        maxArgs = 100000;
        identifiers.add("tell");
        identifiers.add("ch tell");
        identifiers.add("msg");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player teller = (Player) sender;
            ConversationManager cm = plugin.getConversationManager();
            if (args.length == 1) {
                Player tellee = plugin.getServer().getPlayer(args[0]);
                if (tellee != null) {
                    if (tellee != teller) {
                        cm.start(teller, tellee);
                        teller.sendMessage(plugin.getTag() + "§cStarted conversation with " + tellee.getName());
                    }
                } else {
                    teller.sendMessage(plugin.getTag() + "§cPlayer not found");
                }
            } else if (args.length == 0) {
                if (cm.hasActive(teller)) {
                    cm.end(teller);
                    teller.sendMessage(plugin.getTag() + "§cEnded your conversation");
                }
            } else {
                Player tellee = plugin.getServer().getPlayer(args[0]);
                if (tellee != null) {
                    if (tellee != teller) {
                        String msg = "";
                        for (int i = 1; i < args.length; i++) {
                            msg += args[i] + " ";
                        }
                        String outgoing = Messaging.format(plugin, null, plugin.getOutgoingTellFormat(), teller.getName(), tellee.getName(), msg, true, plugin.getPermissionManager().isAllowedColor(teller));
                        String incoming = Messaging.format(plugin, null, plugin.getIncomingTellFormat(), teller.getName(), tellee.getName(), msg, true, plugin.getPermissionManager().isAllowedColor(teller));
                        tellee.sendMessage(incoming);
                        teller.sendMessage(outgoing);
                        plugin.log(Level.INFO, teller.getName() + " -> " + tellee.getName() + ": " + msg);
                    }
                } else {
                    teller.sendMessage(plugin.getTag() + "§cPlayer not found");
                }
            }
        }
    }
}
