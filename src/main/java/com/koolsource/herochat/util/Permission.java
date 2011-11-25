/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/
package com.koolsource.herochat.util;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;

import java.util.List;

public class Permission {

    private PermissionManager security;

    public Permission(PermissionManager security) {
        this.security = security;
    }

    public String[] getGroups(Player p) {
        if (security != null) {
            return security.getUser(p).getGroupsNames();
        }
        return new String[0];
    }

    public boolean anyGroupsInList(Player p, List<String> list) {
        String[] groups = getGroups(p);
        for (int i = 0; i < groups.length; i++) {
            if (list.contains(groups[i])) {
                return true;
            }
        }
        return false;
    }

    public String getGroup(Player p) {
        if (security != null) {
            String group = security.getUser(p).getGroupsNames()[0];
            if (group == null) {
                group = "";
            }
            return group;
        } else {
            return "";
        }
    }

    public String getGroupPrefix(Player p) {
        if (security != null) {
            return security.getGroup(this.getGroup(p)).getPrefix().replaceAll("&([0-9a-f])", "§$1");
        }
        return "";
    }

    public String getGroupSuffix(Player p) {
        if (security != null) {
            return security.getGroup(this.getGroup(p)).getSuffix().replaceAll("&([0-9a-f])", "§$1");
        }
        return "";
    }

    public String getPrefix(Player p) {
        if (security != null) {
            return security.getUser(p).getPrefix().replaceAll("&([0-9a-f])", "§$1");
        }
        return "";
    }

    public String getSuffix(Player p) {
        if (security != null) {
            return security.getUser(p).getSuffix().replaceAll("&([0-9a-f])", "§$1");
        }
        return "";
    }

    public boolean isAdmin(Player p) {
        if (security != null) {
            return security.has(p, "herochat.admin");
        } else {
            return true;
        }
    }

    public boolean isAllowedColor(Player p) {
        if (security != null) {
            return security.has(p, "herochat.color");
        } else {
            return true;
        }
    }

    public boolean canCreate(Player p) {
        if (security != null) {
            boolean admin = security.has(p, "herochat.admin");
            boolean create = security.has(p, "herochat.create");
            return admin || create;
        } else {
            return true;
        }
    }
}
