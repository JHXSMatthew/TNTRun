package com.mcndsj.TNTRun.commands;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GameState;
import com.mcndsj.TNTRun.game.gameMap.GameMap;
import com.mcndsj.TNTRun.manager.GameManager;
import com.mcndsj.TNTRun.manager.PlayerManager;
import com.mcndsj.TNTRun.utils.LocationFactory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 29/06/2016.
 */
public class tntCommand implements CommandExecutor {

    private GameMap map;
    private boolean count = false;

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.isOp() || !(commandSender instanceof Player)){
            return true;
        }
        if(strings == null || strings.length < 1){
            showHelp(commandSender);
            return true;
        }

        String label = strings[0];

        if(label.equals("game")){
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
            commandSender.sendMessage(map.getName());
            return true;
        }
        switch (label) {
            case "spawn":
                map.setSpawn(new LocationFactory(((Player) commandSender).getLocation()));
                commandSender.sendMessage("spawn done!");
                break;
            case "loc":
                map.addBounds(((Player) commandSender).getLocation());
                commandSender.sendMessage(String.valueOf(count));
                count = true;
                break;
            case "save":
                count = false;
                map.save();
                map = null;
                commandSender.sendMessage("success!");
                break;
            case "world":
                if (strings.length < 2) {
                    showHelp(commandSender);
                    return true;
                }
                if (Bukkit.getWorld(strings[1]) == null)
                    Bukkit.createWorld(new WorldCreator(strings[1]));
                ((Player) commandSender).teleport(Bukkit.getWorld(strings[1]).getSpawnLocation());
                ((Player) commandSender).setGameMode(GameMode.SPECTATOR);
                break;
            case "start":
                Game game = PlayerManager.get().getControlPlayer(commandSender.getName()).getGame();
                game.switchState(GameState.starting);
                Core.get().getLogger().info("Game " + game.toString() + " switched to starting due to command.");
                break;
            default:
                showHelp(commandSender);
                break;
        }
        return true;
    }

    private void showHelp(CommandSender sender){
        sender.sendMessage("- /tnt game <Name> = new game ");
        sender.sendMessage("- /tnt spawn = set spawn point");
        sender.sendMessage("- /tnt loc = set bound");
        sender.sendMessage("- /tnt save = save and clean");
        sender.sendMessage("- /tnt world <worldName> = save and clean");
        sender.sendMessage("- /tnt start");

    }
}
