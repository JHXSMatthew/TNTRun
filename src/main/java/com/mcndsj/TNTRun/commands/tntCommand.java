package com.mcndsj.TNTRun.commands;

import com.mcndsj.TNTRun.game.GameMap;
import com.mcndsj.TNTRun.utils.LocationFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 29/06/2016.
 */
public class tntCommand implements CommandExecutor {

    GameMap map;
    boolean count = false;

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.isOp() || !(commandSender instanceof Player)){
            return true;
        }
        if(strings == null || strings.length < 1){
            showHelp(commandSender);
            return true;
        }

        String label = strings[0];

        if(label.equals("game")){ //// TODO: 2016/6/30 [DISCUSS] "game" or "name", typo? -- Mulan Lin
            if(strings.length < 2){
                showHelp(commandSender);
                return true;
            }
            if(map != null){
                commandSender.sendMessage("Game not null!");
                return true;
            }
            map = new GameMap((Player) commandSender);
            map.setName(strings[1]);
            return true;
        }
        if(label.equals("spawn")){
            map.setSpawn(new LocationFactory(((Player) commandSender).getLocation()));
        }else if(label.equals("loc")){
            map.addBounds(((Player) commandSender).getLocation());
            commandSender.sendMessage(String.valueOf(count));
            count = true;
        }else if(label.equals("save")){
            count = false;
            map.save();
            map = null;
            commandSender.sendMessage("success!");
        }else showHelp(commandSender);
        return true;
    }

    private void showHelp(CommandSender sender){
        sender.sendMessage("- /tnt name <Name> = new game ");
        sender.sendMessage("- /tnt spawn = set spawn point");
        sender.sendMessage("- /tnt loc = set bound");
        sender.sendMessage("- /tnt save = save and clean");

    }
}
