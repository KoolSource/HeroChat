/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.koolsource.herochat.channels.Channel;
import com.koolsource.herochat.channels.ChannelManager;
import com.koolsource.herochat.channels.ConversationManager;
import com.koolsource.herochat.util.Messaging;

public class HeroChatPlayerListener extends PlayerListener {

    private HeroChat plugin;

    public HeroChatPlayerListener(HeroChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String input = event.getMessage().substring(1);
        String[] args = input.split(" ");
        Channel c = plugin.getChannelManager().getChannel(args[0]);
        if (c != null && c.isQuickMessagable()) {
            event.setCancelled(true);
            plugin.getCommandManager().dispatch(event.getPlayer(), null, "qm", args);
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player sender = event.getPlayer();
        String senderName = sender.getName();
        ConversationManager convos = plugin.getConversationManager();
        if (convos.hasActive(sender)) {
            Player receiver = convos.getTellee(sender);
            String receiverName = receiver.getName();
            if (!plugin.getChannelManager().isIgnoring(receiverName, senderName)) {
                String message = event.getMessage();
                String outgoing = Messaging.format(plugin, null, plugin.getOutgoingTellFormat(), senderName, receiverName, message, true, plugin.getPermissionManager().isAllowedColor(sender));
                String incoming = Messaging.format(plugin, null, plugin.getIncomingTellFormat(), senderName, receiverName, message, true, plugin.getPermissionManager().isAllowedColor(sender));
                receiver.sendMessage(incoming);
                sender.sendMessage(outgoing);
                plugin.log(Level.INFO, senderName + " -> " + receiverName + ": " + message);
            } else {
                sender.sendMessage(plugin.getTag() + "§c" + receiverName + " is ignoring you");
            }
        } else {
            ChannelManager cm = plugin.getChannelManager();
            Channel c = cm.getActiveChannel(senderName);
            if (c != null) {
                if (!c.getPlayers().contains(senderName)) {
                    c.addPlayer(senderName);
                }
                c.sendMessage(senderName, event.getMessage());
            }
        }
        event.setCancelled(true);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joiner = event.getPlayer();
        String name = joiner.getName();
        try {
            plugin.getConfigManager().loadPlayer(name);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quitter = event.getPlayer();
        String quitterName = quitter.getName();
        plugin.getConfigManager().savePlayer(quitterName);
        plugin.getChannelManager().removeFromAll(quitterName);
        plugin.getChannelManager().setActiveChannel(quitterName, null);

        ConversationManager convos = plugin.getConversationManager();
        if (convos.hasActive(quitter)) {
            Player tellee = convos.getTellee(quitter);
            if (convos.isActive(tellee, quitter)) {
                convos.end(tellee);
                tellee.sendMessage(plugin.getTag() + "§cEnded your conversation (Player logged out)");
            }
            convos.end(quitter);
        }
    }
}
