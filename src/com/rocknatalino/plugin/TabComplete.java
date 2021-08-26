package com.rocknatalino.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        List<String> l = new ArrayList<>();
        if(args.length == 1) {
           l.add("setdisplayname");
           l.add("require-permission");
           l.add("settings");
           l.add("reload");
        }


            if(args[0].equals("settings") && args.length == 2){
                l.add("off");
                l.add("on");
            }
        if(args[0].equals("requirepermission") && args.length == 2){
            l.add("false");
            l.add("true");
        }



        return l;
    }
    // TODO Auto-generated method stub
}
