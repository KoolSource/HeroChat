/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.channels;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;

import com.koolsource.herochat.HeroChat;
import com.koolsource.herochat.event.ChannelChatEvent;

public class LocalChannel extends Channel {

    protected int distance;

    public LocalChannel(HeroChat plugin) {
        super(plugin);
        distance = 100;
    }

    @Override
    public void sendMessage(String name, String msg) {
        ChannelChatEvent event = new ChannelChatEvent(Type.CUSTOM_EVENT, this, name, msg, msgFormat, true);
        plugin.getServer().getPluginManager().callEvent(event);
        name = event.getSource();
        msg = event.getMessage();
        String format = event.getFormat();
        boolean sentByPlayer = event.isSentByPlayer();
        if (!event.isCancelled()) {
            Player sender = plugin.getServer().getPlayer(name);
            if (sender != null) {
                if (enabled || plugin.getPermissionManager().isAdmin(sender) || moderators.contains(name)) {
                    if (plugin.getPermissionManager().anyGroupsInList(sender, voicelist) || voicelist.isEmpty()) {
                        if (!plugin.getChannelManager().getMutelist().contains(sender.getName())) {
                            if (!mutelist.contains(sender.getName())) {
                                if (worlds.isEmpty() || worlds.contains(sender.getWorld().getName())) {
                                    List<String> recipients = getListeners(sender);
                                    boolean color = plugin.getPermissionManager().isAllowedColor(sender);
                                    sendUncheckedMessage(name, msg, format, sentByPlayer, recipients, true, color);

                                    if (recipients.size() == 1) {
                                        sender.sendMessage("§8No one hears you.");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getTag() + "You are not in the correct world for " + getCName());
                                }
                            } else {
                                sender.sendMessage(plugin.getTag() + "You are muted in " + getCName());
                            }
                        } else {
                            sender.sendMessage(plugin.getTag() + "You are globally muted");
                        }
                    } else {
                        sender.sendMessage(plugin.getTag() + "You cannot speak in " + getCName());
                    }
                } else {
                    sender.sendMessage(plugin.getTag() + "This channel is disabled");
                }
            }
        }
    }

    private List<String> getListeners(Player origin) {
        List<String> list = new ArrayList<String>();
        Location sLoc = origin.getLocation();
        String sWorld = sLoc.getWorld().getName();
        for (String name : players) {
            Player player = plugin.getServer().getPlayer(name);
            if (player != null) {
                if (!plugin.getChannelManager().isIgnoring(name, origin.getName())) {
                    Location pLoc = player.getLocation();
                    if (sWorld.equals(pLoc.getWorld().getName())) {
                        int dx = sLoc.getBlockX() - pLoc.getBlockX();
                        int dz = sLoc.getBlockZ() - pLoc.getBlockZ();
                        dx = dx * dx;
                        dz = dz * dz;
                        int d = (int) Math.sqrt(dx + dz);

                        if (d <= distance) {
                            list.add(player.getName());
                        }
                    }
                }
            }
        }
        return list;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
