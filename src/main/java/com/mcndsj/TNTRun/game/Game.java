package com.mcndsj.TNTRun.game;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.UsedI.IReceiver;
import com.mcndsj.TNTRun.game.counters.EndGameCounter;
import com.mcndsj.TNTRun.game.counters.InGameCounter;
import com.mcndsj.TNTRun.game.counters.StartingCounter;
import com.mcndsj.TNTRun.game.gameMap.IGameMap;
import com.mcndsj.TNTRun.manager.PlayerManager;
import org.bukkit.*;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 19/06/2016.
 */
@Getter
public class Game implements IReceiver{


    private int id;
    private List<GamePlayer> inGame = new ArrayList<GamePlayer>();
    private GameState gameState = null;
    private IGameMap map;
    private BukkitRunnable currentCounter;


    public Game(IGameMap map, int id){
        //load map
        this.id = id;
        this.map = map;
        map.load();
        switchState(GameState.lobby);
    }






    public void gameJoin(Player p){
        GamePlayer gp  = PlayerManager.get().getControlPlayer(p.getName());
        inGame.add(gp);
        gp.setGame(this);
        gp.setPostJoin();
        show(gp);
        p.teleport(map.getLobby());
        sendMessage(ChatColor.GRAY  + "玩家 "+gp.getName() +" 加入了游戏!" + ChatColor.GREEN + " ("+inGame.size() +"/"+ Bukkit.getMaxPlayers()+")" );
        if(inGame.size() == Config.playerPerGame && gameState == GameState.lobby && inGame.size() != 1) // pre-condition check
            switchState(GameState.starting);

    }

    public void gameQuit(Player p){
        GamePlayer gp  = PlayerManager.get().getControlPlayer(p.getName());
        if(gp == null){
            return;
        }
        inGame.remove(gp);
        gp.setGame(null);
        sendMessage(  ChatColor.GRAY  + "玩家 "+gp.getName() +" 离开了游戏!" );
        gp.sendToLobby();
        gp.get().setGameMode(GameMode.SPECTATOR);
        checkWinning();
        hide(gp);
    }

    public void checkWinning(){
        if(inGame.size() == 1 && gameState == GameState.inGaming){
            switchState(GameState.end);
            return;
        }

        byte b = 0;
        for(GamePlayer gp : inGame){
            if(gp.get().getGameMode() != GameMode.SPECTATOR){
                b++;
            }
        }
        if(b == 1 && gameState == GameState.inGaming)
            switchState(GameState.end);

    }

    public void onVoidDamage(Player gp){
        if(gameState == GameState.inGaming){
            gp.setGameMode(GameMode.SPECTATOR);
            gp.sendMessage(" 你死了..没关系,再来一局一定能赢!");
            sendMessage(ChatColor.RED + " 玩家 " + ChatColor.GREEN + gp.getName() + ChatColor.RED +  " 已死亡. 当前剩余玩家数:" +  ChatColor.GREEN + getSurvivals());
            sendTitle(ChatColor.RED + "已死亡", ChatColor.RED + "You Die");
            gp.teleport(map.getSpawn());
        }else if(gameState == GameState.end){
            gp.teleport(map.getSpawn());
            gp.setGameMode(GameMode.SPECTATOR);
        }else{
            gp.teleport(map.getLobby());
        }
    }

    private int getSurvivals(){
        int count = 0;
        for(GamePlayer gp : inGame){
            if(gp.get().getGameMode() != GameMode.SPECTATOR){
                count ++;
            }
        }
        return count;
    }


    private void cancelTask(){
        try{
            currentCounter.cancel();
        }catch(Exception e){

        }
    }

    private void runTask(){
        try{
            currentCounter.runTaskTimer(Core.get(),0,20);
        }catch(Exception e){

        }
    }


    public void switchState(GameState state){
        cancelTask();

        switch (state){
            case lobby:
                this.gameState = GameState.lobby;
                break;
            case starting:
                this.gameState = GameState.starting;
                sendMessage(ChatColor.GREEN + "游戏即将开始,请做好准备!");
                currentCounter = new StartingCounter(this);
                break;
            case inGaming:
                this.gameState = GameState.inGaming;
                setPreGameStart();
                currentCounter = new InGameCounter(this);
                break;
            case end:
                this.gameState = GameState.end;
                sendSound(Sound.WITHER_DEATH);
                GamePlayer winner = getWinner();
                if(winner == null){
                    Bukkit.shutdown();
                }
                winner.get().setAllowFlight(true);
                winner.get().setFlying(true);
                currentCounter = new EndGameCounter(this);
                //TODO: give coins
                //precess win stuff
                break;

        }
    }

    public void dispose(){
        getMap().unload();
    }

    public GamePlayer getWinner(){
        for(GamePlayer gp : inGame){
            if(gp.get().getGameMode() != GameMode.SPECTATOR){
                return gp;
            }
        }
        return null;
    }



    public void sendMessage(String message){
        for(GamePlayer gp : inGame){
            gp.sendMessage(message);
        }
    }

    public void sendTitle(String message, String sub) {
        for(GamePlayer gp : inGame){
            gp.sendTitle(message,sub);
        }
    }

    public void sendActionBar(String message){
        for(GamePlayer gp : inGame){
            gp.sendActionBar(message);
        }
    }

    public void sendSound( Sound s) {
        for(GamePlayer gp : inGame){
            gp.sendSound(s);
        }
    }

    public void teleport(Location l) {
        for(GamePlayer gp : inGame){
            gp.teleport(l);
        }
    }

    public void setPreGameStart() {
        for(GamePlayer gp : inGame){
            gp.setPreGameStart();
            gp.teleport(map.getSpawn());
        }
    }

    public void setPostJoin() {
        for(GamePlayer gp : inGame){
            gp.setPostJoin();
            gp.teleport(map.getLobby());
        }
    }

    public void sendToLobby() {
        for(GamePlayer gp : inGame){
            gp.sendToLobby();
        }
    }


    public void hide(GamePlayer gp){
        for(GamePlayer temp : inGame){
            gp.hide(gp);
        }
    }


    public void show(GamePlayer gp){
        for(GamePlayer temp : inGame){
            gp.show(gp);
        }
    }



}
