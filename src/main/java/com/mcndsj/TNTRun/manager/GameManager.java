package com.mcndsj.TNTRun.manager;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
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
        File dir = new File(Core.get().getDataFolder(), Config.configFolderName);
        File[] files = dir.listFiles();
        if (files == null) return;
        for(File aFile : files){
            GameMap m = new GameMap(aFile);
            gmap.add(m);
            Core.get().getLogger().info("GameMap " +  m.getName()  + " loaded !");
        }

    }



    public void nextNewGame(){
        try {
            //random get a map
            Collections.shuffle(gmap);
            currentGame = new Game(gmap.get(0));
        }catch(Exception e){

        }

    }

}
