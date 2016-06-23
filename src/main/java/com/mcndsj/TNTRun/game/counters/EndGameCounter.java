package com.mcndsj.TNTRun.game.counters;

import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
import com.mcndsj.TNTRun.utils.BungeeUtils;
import com.mcndsj.TNTRun.utils.FireWorkUlt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matthew on 19/06/2016.
 */
public class EndGameCounter extends BukkitRunnable {


    private static int maxCount = 31;
    private int current = 0;
    Game game;

    public EndGameCounter(Game game){
        current = maxCount;
        this.game = game;
    }


    public void run() {
        current --;
        if(current == 0){
            game.sendToLobby();
        }else{
            GamePlayer gp  = game.getWinner() ;
            if(gp != null && gp.get() != null && gp.get().isOnline()){
                FireWorkUlt.spawnFireWork(gp.get().getLocation(),gp.get().getWorld());
                gp.sendTitle(ChatColor.RED + "你赢了!",null);
            }

        }

        game.sendActionBar(ChatColor.GREEN + "你将在 " + current+ " 秒内被传送回大厅");
    }
}
