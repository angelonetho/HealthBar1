package com.rocknatalino.plugin;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.entity.Entity;

public class MythicMobsCompatibility {
    public boolean MythicMobShow(Entity entity) {
        return MythicBukkit.inst().getAPIHelper().isMythicMob(entity);
    }
}
