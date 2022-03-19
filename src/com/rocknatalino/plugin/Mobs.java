package com.rocknatalino.plugin;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Mobs implements Listener {

    final Main main;
    final HashMap<UUID, String> mobs = new HashMap<>();
    String mobDisplayName;
    List<String> blockedWorlds;
    int secondsToHide, style;
    String[] defaultBar = new String[21];

    public Mobs(Main main) {
        this.main = main;
        mobDisplayName = Objects.requireNonNull(main.getConfig().getString("style-display")).replaceAll("&", "§");
        mobDisplayName = mobDisplayName.replaceAll("<3", "❤");
        blockedWorlds = main.getConfig().getStringList("disabled-worlds");
        secondsToHide = main.getConfig().getInt("mobs-fade-out");
        style = main.getConfig().getInt("mobs-style");
    }

    private String generateBarString(int style, int health, int maxHealth, String entityName){
        String bar = "Err";

        if(style != 2) {
            String aux = mobDisplayName.replaceAll("%entityName%", String.valueOf(entityName));
            aux = aux.replaceAll("%health%", String.valueOf(health));
            aux = aux.replaceAll("%maxHealth%", String.valueOf(maxHealth));
            bar = aux;

        }else {
            int scaledHealth = health * 20 / maxHealth;

            if(scaledHealth < 0) return bar;
            defaultBar[0] = "§c▌         ";
            defaultBar[1] = "§c█         ";
            defaultBar[2] = "§c█▌        ";
            defaultBar[3] = "§c██        ";
            defaultBar[4] = "§c██▌       ";
            defaultBar[5] = "§c███       ";
            defaultBar[6] = "§e███▌      ";
            defaultBar[7] = "§e████      ";
            defaultBar[8] = "§e████▌     ";
            defaultBar[9] = "§e█████     ";
            defaultBar[10] ="§e█████▌    ";
            defaultBar[11] ="§e██████    ";
            defaultBar[12] ="§a██████▌   ";
            defaultBar[13] ="§a███████   ";
            defaultBar[14] ="§a███████▌  ";
            defaultBar[15] ="§a████████  ";
            defaultBar[16] ="§a████████▌ ";
            defaultBar[17] ="§a█████████ ";
            defaultBar[18] ="§a█████████▌";
            defaultBar[19] ="§a██████████";
            defaultBar[20] ="§a██████████▌";

            bar = defaultBar[scaledHealth];

        }

        return bar;
    }

    @EventHandler
    public void onHit(EntityDamageEvent event) {

        if (!main.getConfig().getBoolean("mobs")) return;

        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if(entity.getType() != EntityType.ENDER_DRAGON && entity.getType() != EntityType.WITHER && entity.getType() != EntityType.PLAYER)
            if (!blockedWorlds.contains(entity.getWorld().getName())) {
                String entityName;
                if (!mobs.containsKey(entity.getUniqueId())) {

                    if(entity.getCustomName() != null){
                        entityName = entity.getCustomName();
                    }else {
                        entityName = entity.getName();
                    }

                    mobs.put(entity.getUniqueId(), entity.getCustomName());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                        entity.setCustomNameVisible(false);
                        entity.setCustomName(mobs.get(entity.getUniqueId()));
                        mobs.remove(entity.getUniqueId());
                    }, secondsToHide * 20L);
                }else {
                    if(mobs.get(entity.getUniqueId()) == null){
                        entityName = StringUtils.capitalize(entity.getType().toString().toLowerCase());
                    }else {
                        entityName = mobs.get(entity.getUniqueId());
                    }
                }

                entity.setCustomNameVisible(true);
                entity.setCustomName(generateBarString(style, (int) entity.getHealth() - (int) event.getFinalDamage(), (int) entity.getMaxHealth(), entityName));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKillEvent(EntityDamageByEntityEvent event)  {
        if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Projectile) {
            LivingEntity killed = (LivingEntity) event.getEntity();
            Entity killer = (Entity) ((Projectile) event.getDamager()).getShooter();

            assert killer != null;
            UUID killerUUID = killer.getUniqueId();
            boolean fatalDamage = event.getFinalDamage() >= killed.getHealth();

            if (fatalDamage) {
                if (!blockedWorlds.contains(killer.getWorld().getName())) {
                    if (mobs.containsKey(killerUUID)) {
                        killer.setCustomName(mobs.get(killerUUID));
                        killer.setCustomNameVisible(false);
                        mobs.remove(killerUUID);
                    }
                }
            }
        }
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity killed = (LivingEntity) event.getEntity();
            Entity killer = event.getDamager();

            UUID killerUUID = killer.getUniqueId();
            boolean fatalDamage = event.getFinalDamage() >= killed.getHealth();

            if (fatalDamage) {
                if (!blockedWorlds.contains(killer.getWorld().getName())) {
                    if (mobs.containsKey(killerUUID)) {
                        killer.setCustomName(mobs.get(killerUUID));
                        killer.setCustomNameVisible(false);
                        mobs.remove(killerUUID);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKilledEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity killed = (LivingEntity) event.getEntity();

            UUID killedUUID = killed.getUniqueId();
            boolean fatalDamage = event.getFinalDamage() >= killed.getHealth();

            if (fatalDamage) {
                if (!blockedWorlds.contains(killed.getWorld().getName())) {
                    if (mobs.containsKey(killedUUID)) {
                        killed.setCustomName(mobs.get(killedUUID));
                        killed.setCustomNameVisible(false);
                        mobs.remove(killedUUID);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cleanCache(PluginDisableEvent event) {
        Bukkit.getConsoleSender().sendMessage("cleaning cache...");
        mobs.forEach((uuid, s) -> {
            if (Bukkit.getEntity(uuid) != null) {
                Objects.requireNonNull(Bukkit.getEntity(uuid)).setCustomName(s);
                Objects.requireNonNull(Bukkit.getEntity(uuid)).setCustomNameVisible(false);
            }
        });
    }
}
