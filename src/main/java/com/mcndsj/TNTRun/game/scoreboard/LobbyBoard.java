package com.mcndsj.TNTRun.game.scoreboard;

import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
import com.mcndsj.TNTRun.game.counters.StartingCounter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 9/07/2016.
 */
public class LobbyBoard implements IBoard {

    private Game game;
    private IndexBasedBoard board;

    public LobbyBoard(Game game){
        this.game = game;
        board = new IndexBasedBoard("TNTRun");
        init();
        for(GamePlayer p : game.getInGame()){
            addPlayer(p);
        }
    }





    public void addPlayer(GamePlayer gp){
        board.send(gp.get());
    }

    public void removePlayer(GamePlayer gp){

    }

    @Override
    public void updateCount() {
        board.add("玩家: " + game.getInGame().size() +"/" + Config.playerPerGame, 5);
        board.update();

    }

    @Override
    public void dispose() {
        board.reset();
    }

    @Override
    public void updateCounter() {
        if(game.getGameState() == GameState.starting){
            board.add("距离开始: " + ChatColor.GREEN + ((StartingCounter)game.getCurrentCounter()).getCount(), 3);
            board.update();
        }

    }

    /**
     *  full update, expensive.
     * @param state the state to
     */
    public void updateScoreboard(GameState state){
        switch(state){
            case lobby:
                init();
                break;
            case starting:
                updateCounter();
                break;
            default:
                System.err.println("wtf??");
        }
    }

    @Override
    public void init() {
        board.add("地图: " + ChatColor.YELLOW + game.getMap().getName(), 7);
        board.add(" ", 6);
        board.add("玩家: " + ChatColor.GREEN + + game.getInGame().size() +"/" + Config.playerPerGame, 5);
        board.add("  ", 4);
        board.add(ChatColor.YELLOW +"人数不足", 3);
        board.add("   ", 2);

        board.add(ChatColor.AQUA + "www.mcndsj.com", 1);
        board.update();
    }







}
