package com.mcndsj.TNTRun.game.counters;

import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matthew on 19/06/2016.
 */
public class StartingCounter extends BukkitRunnable {


    private static int maxCount = 31;
    private int current = 0;
    Game game;

    public StartingCounter(Game game){
        current = maxCount;
        this.game = game;
    }


    public void run() {
        current --;
        if(current == 0){
            game.switchState(GameState.inGaming);
        }else{
            if(game.getInGame().size() < Bukkit.getMaxPlayers()/2){
                game.switchState(GameState.lobby);
                game.sendMessage(ChatColor.RED + "人数不足,请等待更多玩家加入!");
            }
        }
        //send action Bar
        if(current %10 == 0 || current < 10){
            game.sendSound(Sound.CLICK);
            game.sendTitle(null, ChatColor.GREEN + "还有 " + current + " 开始");
        }

        game.sendActionBar(ChatColor.GREEN + "== 距离游戏开始 " + current+ " 秒 ==");
    }
}
