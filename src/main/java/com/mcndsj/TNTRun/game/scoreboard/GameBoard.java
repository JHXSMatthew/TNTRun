package com.mcndsj.TNTRun.game.scoreboard;

import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
import com.mcndsj.TNTRun.game.counters.TimeGetable;
import org.bukkit.ChatColor;

/**
 * Created by Matthew on 9/07/2016.
 */
public class GameBoard implements IBoard{

    private Game game;
    private IndexBasedBoard board;
    private int lastTime;

    public GameBoard(Game game){
        this.game = game;
        board = new IndexBasedBoard("TNTRun");
        init();
        for(GamePlayer p : game.getInGame()){
            addPlayer(p);
        }
    }



    @Override
    public void init() {
        board.add("地图: " + ChatColor.YELLOW + game.getMap().getName(), 7);
        board.add(" ", 6);
        board.add("幸存者: " + ChatColor.GREEN + + game.getLivePlayerCount() +"/" + game.getInGame().size(), 5);
        board.add("  ", 4);
        board.add("跑酷时间: " + ChatColor.YELLOW +getTimeString(0) , 3);
        board.add("   ", 2);
        board.add(ChatColor.AQUA + "www.mcndsj.com", 1);
        board.update();
    }

    private String getTimeString(int seconds){
        StringBuilder sb = new StringBuilder();
        if(seconds > 60) {
            sb.append(seconds / 60).append("分").append(seconds%60).append("秒");
        }else{
            sb.append(seconds).append("秒");
        }
        return sb.toString();
    }

    public void addPlayer(GamePlayer gp){
        board.send(gp.get());
    }

    public void removePlayer(GamePlayer gp){

    }

    @Override
    public void updateCount() {
        board.add("幸存者: " + ChatColor.GREEN + + game.getLivePlayerCount() +"/" + game.getInGame().size(), 5);
        board.update();
    }

    @Override
    public void dispose() {
        board.reset();
    }

    @Override
    public void updateCounter() {
        switch (game.getGameState()) {
            case inGaming:
                board.add("时间: " + ChatColor.YELLOW + getTimeString(((TimeGetable) game.getCurrentCounter()).getTime()), 3);
                board.update();
                lastTime = ((TimeGetable) game.getCurrentCounter()).getTime();
                break;
        }
    }

    /**
     *  full update, expensive.
     * @param state the state to
     */
    public void updateScoreboard(GameState state){
        switch(state){
            case inGaming:
                break;
            case end:
                board.add("地图: " + ChatColor.YELLOW + game.getMap().getName(), 7);
                board.add(" ", 6);
                board.add("获胜者: " + ChatColor.GREEN + game.getWinner().getName(), 5);
                board.add("  ", 4);
                board.add("跑酷时间: " + ChatColor.YELLOW + getTimeString(lastTime) , 3);
                board.add("   ", 2);
                board.add(ChatColor.AQUA + "www.mcndsj.com", 1);
                board.update();
                break;
        }
    }




}
