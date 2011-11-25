/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.ensifera.animosity.craftirc.IRCEvent;
import com.koolsource.herochat.channels.Channel;

public class HeroChatCraftIRCListener extends CustomEventListener implements Listener {

    private HeroChat plugin;

    public HeroChatCraftIRCListener(HeroChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCustomEvent(Event event) {
        if (event instanceof IRCEvent) {
            IRCEvent ircEvent = (IRCEvent) event;
            if (!ircEvent.isHandled()) {
                switch (ircEvent.eventMode) {
                    case MSG:
                        String msg = ircEvent.msgData.message;
                        String sender = ircEvent.msgData.sender;
                        String channelTag = ircEvent.msgData.srcChannelTag;
                        String ircTag = plugin.getIrcTag();
                        Channel[] channels = plugin.getChannelManager().getChannels().toArray(new Channel[0]);
                        for (Channel c : channels) {
                            if (c.getIRCToGameTags().contains(channelTag)) {
                                c.sendMessage(ircTag.replaceAll("&([0-9a-f])", "§$1") + sender, msg, c.getMsgFormat(), false);
                            }
                        }
                        ircEvent.setHandled(true);
                        break;
                    case JOIN:
                        msg = ircEvent.msgData.message;
                        sender = ircEvent.msgData.sender;
                        channelTag = ircEvent.msgData.srcChannelTag;
                        ircTag = plugin.getIrcTag();
                        channels = plugin.getChannelManager().getChannels().toArray(new Channel[0]);
                        for (Channel c : channels) {
                            if (c.getIRCToGameTags().contains(channelTag) && c.isVerbose()) {
                                String joinMsg = "§f" + ircTag + sender + c.getColor().str + " has joined the channel";
                                c.sendMessage(ircTag.replaceAll("&([0-9a-f])", "§$1") + sender, joinMsg, Channel.joinFormat, false, false);
                            }
                        }
                        ircEvent.setHandled(true);
                        break;
                    case BAN:
                    case KICK:
                    case PART:
                    case QUIT:
                        msg = ircEvent.msgData.message;
                        sender = ircEvent.msgData.sender;
                        channelTag = ircEvent.msgData.srcChannelTag;
                        ircTag = plugin.getIrcTag();
                        channels = plugin.getChannelManager().getChannels().toArray(new Channel[0]);
                        for (Channel c : channels) {
                            if (c.getIRCToGameTags().contains(channelTag) && c.isVerbose()) {
                                String leaveMsg = "§f" + ircTag + sender + c.getColor().str + " has left the channel";
                                c.sendMessage(ircTag.replaceAll("&([0-9a-f])", "§$1") + sender, leaveMsg, Channel.joinFormat, false, false);
                            }
                        }
                        ircEvent.setHandled(true);
                        break;
                }
            }
        }
    }
}
