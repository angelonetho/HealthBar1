package com.rocknatalino.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class Main extends JavaPlugin implements Listener {

    Scoreboard board;
    Objective objective;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        if (getConfig().getBoolean("mobs")) { getServer().getPluginManager().registerEvents(new Mobs(this), this);}
        getCommand("HealthBar").setExecutor(new Commands(this));
        getCommand("HealthBar").setTabCompleter(new TabComplete());
        createHealthBar();

        int pluginId = 12564;
        new Metrics(this, pluginId);

    }

    @Override
    public void onDisable() {
        objective.unregister();
        Bukkit.getConsoleSender().sendMessage("§e[HealthBar] Configuration file saved.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (getConfig().getBoolean("require-permission")) {
            if (e.getPlayer().hasPermission("healthbar.see")) {
                e.getPlayer().setScoreboard(board);
            }
        } else {
            e.getPlayer().setScoreboard(board);
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        checkDisabledWorld(e.getPlayer(), e.getPlayer().getWorld().getName());
    }

    public void checkDisabledWorld(Player player, String worldName) {
        List<String> s = getConfig().getStringList("disabled-worlds");
        if (s.contains(worldName)) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        } else {
            player.setScoreboard(board);
        }
    }

    public void createHealthBar() {
        reloadConfig();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        board = manager.getNewScoreboard();
        String displayName = getConfig().getString("displayName").replaceAll("&", "§");
        displayName = displayName.replaceAll("<3", "❤");
        //objective = board.registerNewObjective("HealthBar", "health",displayName, RenderType.INTEGER);
        objective = board.registerNewObjective("healthBar", "health");
        objective.setDisplayName(displayName);
        //objective.setRenderType(RenderType.INTEGER);

        if (getConfig().getBoolean("players")) {

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        } else {
            board.clearSlot(DisplaySlot.BELOW_NAME);

            Bukkit.getConsoleSender().sendMessage("§eHealthBar is disabled in config.yml");
        }

        if (getConfig().getBoolean("require-permission")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.hasPermission("healthbar.see")) {
                    checkDisabledWorld(player, player.getWorld().getName());
                }else {
                    //Delete current scoreboard
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                }
            });
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> checkDisabledWorld(player, player.getWorld().getName()));
        }
    }

    protected void reloadMobConfig(){
        Bukkit.getPluginManager().callEvent(new PluginDisableEvent(this));
        if (getConfig().getBoolean("mobs")) { getServer().getPluginManager().registerEvents(new Mobs(this), this);}
    }




}

