package com.mcndsj.TNTRun.manager;

import com.avaje.ebean.validation.NotNull;
import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GameMap;
import com.mcndsj.TNTRun.game.GamePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matthew on 19/06/2016.
 */

public class GameManager {

    private static GameManager manager;
    private Game currentGame;
    private List<GameMap> gmap = new ArrayList<GameMap>();


    public GameManager(){
        File dir = new File(Core.get().getDataFolder(), Config.configFolderName);
        if(!dir.exists()) {
            dir.mkdir();
            dir = new File(Core.get().getDataFolder(), Config.mapFolderName);
            dir.mkdir();
            Config.setUp = true;
            return;

        }

        File[] files = dir.listFiles();
        if (files == null) {
            Config.setUp = true;
            return;
        }
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

    public void gameJoin(Player p){
        currentGame.gameJoin(p);
        //TODO: multi-arena support?
    }

    public void gameQuit(Player p ){
        GamePlayer gp = PlayerManager.get().getControlPlayer(p.getName());
        gp.getGame().gameQuit(p);
    }

    public static GameManager get(){
        if(manager == null) manager = new GameManager();
        return manager;
    }

}
