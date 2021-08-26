package com.rocknatalino.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    final Main main;
    public Commands(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 1){
            if(args[0].equalsIgnoreCase("setdisplayname")) {
                main.getConfig().set("displayName", args[1]);
                main.saveConfig();
                main.createHealthBar();

                sender.sendMessage("§aChanged §2displayName §ato §r" + args[1].replaceAll("&","§") + "§a.");

            }else if(args[0].equalsIgnoreCase("settings")) {
                if(args[1].equalsIgnoreCase("on")){
                    main.getConfig().set("players", true);
                    main.saveConfig();
                    main.createHealthBar();
                    sender.sendMessage("§aHealthBar is now visible");
                }
                if(args[1].equalsIgnoreCase("off")){
                    main.getConfig().set("players", false);
                    main.saveConfig();
                    main.createHealthBar();
                    sender.sendMessage("§aHealthBar is now invisible");
                }
            }else if(args[0].equalsIgnoreCase("require-permission")) {
                if(args[1].equalsIgnoreCase("true")){
                    main.getConfig().set("require-permission", true);
                    main.saveConfig();
                    main.createHealthBar();
                    sender.sendMessage("§2require-permission§a set to§f true");
                }
                if(args[1].equalsIgnoreCase("false")){
                    main.getConfig().set("require-permission", false);
                    main.saveConfig();
                    main.createHealthBar();
                    sender.sendMessage("§2require-permission§a set to§f false");
                    //sender.sendMessage("§7Will affect players when they join again in the server");
                }
            }else {
                help(sender);
            }
        }else if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
            main.reloadConfig();
            main.saveConfig();
            main.createHealthBar();
            sender.sendMessage("§a[HealthBar] Reload complete.");
        } else {
            help(sender);
        }

        return true;
    }

    private void help(CommandSender sender){
        sender.sendMessage("§9§l§m-------------------------");
        sender.sendMessage("§6HealthBar Commands");
        sender.sendMessage("§e/hb setdisplayname <name> - §7§oChanges the value bellow the player name");
        sender.sendMessage("§e/hb require-permission <true/false> - §7§oIs permission required");
        sender.sendMessage("§e/hb settings <on/off> - §7§oToggles health bar visibility");
        sender.sendMessage("§e/hb reload - §7§oReload Config");
        sender.sendMessage("§9§l§m-------------------------");
    }
}

