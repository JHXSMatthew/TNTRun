package com.mcndsj.TNTRun.game.scoreboard;

import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;

/**
 * Created by Matthew on 9/07/2016.
 */
public interface IBoard {

    void updateScoreboard(GameState state);
    void init();
    void addPlayer(GamePlayer gamePlayer);
    void removePlayer(GamePlayer gamePlayer);
    void updateCount(); // player count
    void dispose();
    void updateCounter(); //time
}
