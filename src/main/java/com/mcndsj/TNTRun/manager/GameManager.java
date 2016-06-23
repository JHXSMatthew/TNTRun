package com.mcndsj.TNTRun.manager;

import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GameMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matthew on 19/06/2016.
 */
public class GameManager {

    public Game currentGame;


    public GameManager(){
        List<GameMap> gmap = new ArrayList<GameMap>();

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
