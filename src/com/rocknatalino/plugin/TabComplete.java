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
        if(args.length < 2) {
           l.add("mob");
           l.add("player");
           l.add("reload");
        }

        if(args.length == 2 && args[0].equals("player")){
            l.add("setText");
            l.add("setEnabled");
            l.add("requirePermission");
        }

        if(args.length == 2 && args[0].equals("mob")){
            l.add("setStyle");
            l.add("setText");
            l.add("setEnabled");
        }


        if(args.length == 3 && (args[1].equals("setEnabled") || args[1].equals("requirePermission"))){
            l.add("true");
            l.add("false");
        }

        if(args.length == 3 && (args[1].equals("setStyle"))){
            l.add("1");
            l.add("2");
        }

        return l;
    }
    // TODO Auto-generated method stub
}
