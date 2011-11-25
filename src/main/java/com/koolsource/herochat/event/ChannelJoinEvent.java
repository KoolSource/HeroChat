package com.koolsource.herochat.event;

import org.bukkit.entity.Player;

import com.koolsource.herochat.channels.Channel;

@SuppressWarnings("serial")
public class ChannelJoinEvent extends ChannelEvent {

    protected Player joiner;

    public ChannelJoinEvent(Type type, Channel channel, Player joiner) {
        super(ChannelJoinEvent.class.getSimpleName(), channel);
        this.joiner = joiner;
    }

    public Player getJoiner() {
        return joiner;
    }

    public void setJoiner(Player joiner) {
        this.joiner = joiner;
    }
}
