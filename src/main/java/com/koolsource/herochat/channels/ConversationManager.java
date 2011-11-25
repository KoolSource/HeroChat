package com.koolsource.herochat.channels;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class ConversationManager {

    private HashMap<Player, Player> conversations;

    public ConversationManager() {
        conversations = new HashMap<Player, Player>();
    }

    public HashMap<Player, Player> getConversations() {
        return conversations;
    }

    public boolean hasActive(Player a) {
        return conversations.containsKey(a);
    }

    public boolean isActive(Player a, Player b) {
        Player aPartner = conversations.get(b);
        Player bPartner = conversations.get(a);
        return aPartner != null && bPartner != null;
    }

    public Player getTellee(Player a) {
        Player tellee = conversations.get(a);
        return tellee == null ? null : tellee;
    }

    public void start(Player a, Player b) {
        conversations.put(a, b);
    }

    public void end(Player a) {
        conversations.remove(a);
    }
}
