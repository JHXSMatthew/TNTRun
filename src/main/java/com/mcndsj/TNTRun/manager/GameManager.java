package com.mcndsj.TNTRun.manager;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GameMap;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matthew on 19/06/2016.
 */
public class GameManager {

    public Game currentGame;
    private List<GameMap> gmap = new ArrayList<GameMap>();


    public GameManager(){
        File dir = new File(Core.get().getDataFolder() + "/" + "Maps", "");
        String files[] = dir.list();
        for(String s : files){

            GameMap m = new GameMap(s);
            Core.get().getLogger().info("GameMap " +  gmap.get(gmap.size() -1 ).getName()  + " loaded !");
        }
        try {
            //random get a map
            Collections.shuffle(gmap);
            nextNewGame(gmap.get(0));
        }catch(Exception e){

        }
    }

    public void nextNewGame(GameMap gp){
        currentGame = new Game(gp);
    }

    public void loadMapRecursive(List<GameMap> store){

    }

}
