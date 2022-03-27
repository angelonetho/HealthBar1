package com.rocknatalino.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    final Main main;
    final String mobMessage = "§7When changing mob configuration, some bugs might appear, it's highly recommended to restart the server";
    public Commands(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
            main.reloadConfig();
            //main.saveConfig();
            main.createHealthBar();
            main.reloadMobConfig();
            sender.sendMessage(mobMessage);
            sender.sendMessage("§a[HealthBar] Reload complete.");
            return true;
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("player")) {
            switch (args[1]){
                case "setText":
                    main.getConfig().set("displayName", args[2]);
                    main.saveConfig();
                    main.createHealthBar();
                    sender.sendMessage("§aChanged §2displayName §ato §r" + args[2].replaceAll("&","§") + "§a.");
                    return true;
                case "setEnabled":
                    if(args[2].equalsIgnoreCase("true")){
                        main.getConfig().set("players", true);
                        main.saveConfig();
                        main.createHealthBar();
                        sender.sendMessage("§aHealthBar is now visible for players");
                    }else {
                        main.getConfig().set("players", false);
                        main.saveConfig();
                        main.createHealthBar();
                        sender.sendMessage("§aHealthBar is now invisible for players");
                    }
                    return true;
                case "requirePermission":
                    if(args[2].equalsIgnoreCase("true")){
                        main.getConfig().set("require-permission", true);
                        main.saveConfig();
                        main.createHealthBar();
                        sender.sendMessage("§2require-permission§a set to§f true");
                    }
                    if(args[2].equalsIgnoreCase("false")){
                        main.getConfig().set("require-permission", false);
                        main.saveConfig();
                        main.createHealthBar();
                        sender.sendMessage("§2require-permission§a set to§f false");
                    }
                    return true;
            }
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("mob")) {
            switch (args[1]) {
                case "setStyle":
                    main.getConfig().set("mobs-style", Integer.valueOf(args[2]));
                    main.saveConfig();
                    main.reloadMobConfig();
                    sender.sendMessage(mobMessage);
                    sender.sendMessage("§aChanged §2mobs-style §ato §r" + args[2] + "§a.");
                    return true;
                case "setText":
                    main.getConfig().set("style-display", args[2]);
                    main.saveConfig();
                    main.reloadMobConfig();
                    sender.sendMessage(mobMessage);
                    sender.sendMessage("§aChanged §2displayName §ato §r" + args[2].replaceAll("&", "§") + "§a.");
                    return true;
                case "setEnabled":
                    if (args[2].equalsIgnoreCase("true")) {
                        main.getConfig().set("mobs", true);
                        main.saveConfig();
                        main.reloadMobConfig();
                        sender.sendMessage(mobMessage);
                        sender.sendMessage("§aThe mob's health bar is now visible");
                    } else {
                        main.getConfig().set("mobs", false);
                        main.saveConfig();
                        main.reloadMobConfig();
                        sender.sendMessage(mobMessage);
                        sender.sendMessage("§aThe mob's health bar is now invisible");
                    }
                    return true;
            }
        }
        help(sender);
        return true;
    }

    private void help(CommandSender sender){
        sender.sendMessage("§9§l§m-------------------------");
        sender.sendMessage("§6HealthBar Commands");
        sender.sendMessage("§aPlayer commands:");
        sender.sendMessage("§e/hb player setText <name> - §7§oChanges the value bellow the player name");
        sender.sendMessage("§e/hb player setEnabled <true/false> - §7§oToggles health bar visibility");
        sender.sendMessage("§e/hb player requirePermission <true/false> - §7§oIs permission required");
        sender.sendMessage("§aMob commands:");
        sender.sendMessage("§e/hb mob setStyle <id> - §7§oChanges the style of the bar (1 or 2)");
        sender.sendMessage("§e/hb mob setText <name> - §7§oChanges the value of the mob bar");
        sender.sendMessage("§e/hb mob setEnabled <true/false> - §7§oToggles health bar visibility");
        sender.sendMessage("§aGeneral commands:");
        sender.sendMessage("§e/hb reload - §7§oReload Config");
        sender.sendMessage("§9§l§m-------------------------");
    }

}

