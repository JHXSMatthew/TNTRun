package com.mcndsj.TNTRun.manager;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.gameMap.DecoratedGameMap;
import com.mcndsj.TNTRun.game.gameMap.GameMap;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
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
    private List<Game> games = new ArrayList<Game>();
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



    public Game nextNewGame(){
        try {
            //random get a map
            Collections.shuffle(gmap);
            GameMap chosenMap = gmap.get(0);
            int validId = 0;
            List<Integer> id  = new ArrayList<Integer>();
            for(Game g : games){
                if(g.getMap().getName().equals(chosenMap.getName()))
                    id.add(g.getId());
            }
            if(!id.isEmpty()) {
                if (id.size() != 1) {
                    Collections.sort(id);
                    if (id.get(0) == 0) {
                        for (int i = 1; i < id.size(); i++) {
                            validId = id.get(i) + 1;
                            if (id.get(i) - id.get(i - 1) > 1) {
                                break;
                            }
                        }
                    }
                } else {
                    validId = id.get(0) + 1;
                }
            }
            games.add(new Game(new DecoratedGameMap(validId,gmap.get(0)),validId));

        }catch(Exception e){
            e.printStackTrace();
        }
        return games.get(games.size() - 1);
    }

    public void gameJoin(Player p) {
        for (Game g : games) {
            if (g.getGameState() == GameState.lobby) {
                g.gameJoin(p);
                return;
            }
        }
        nextNewGame().gameJoin(p);
    }

    public void dispose(){
        for(Game g : games){
            g.dispose();
        }
    }

    public void gameQuit(Player p ){
        GamePlayer gp = PlayerManager.get().getControlPlayer(p.getName());
        gp.getGame().gameQuit(p);
    }

    public static GameManager get(){
        if(manager == null) manager = new GameManager();
        return manager;
    }

    public void hide(GamePlayer p){
        for(Game g : games){
            g.hide(p);
        }
    }

    public void show(GamePlayer p){
        for(Game g : games){
            g.show(p);
        }
    }


}
