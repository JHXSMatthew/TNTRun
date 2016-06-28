package com.mcndsj.TNTRun.game.counters;

import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 19/06/2016.
 */
public class InGameCounter extends BukkitRunnable {



    Game game;
    List<Location> cache;

    public InGameCounter(Game game){
        this.game = game;
        cache = new ArrayList<Location>();
    }


    public void run() {
        for(Location l :cache ){
            l.getWorld().playSound(l,Sound.DIG_SAND,1F,1F);
            l.getBlock().setType(Material.AIR);
            l.add(0,-1,0).getBlock().setType(Material.AIR);
        }

        cache.clear();

        for(GamePlayer gp : game.getInGame()){
            if(gp.get().getGameMode() == GameMode.SPECTATOR){
                continue;
            }
            if(game.getMap().rangeCheck(gp.get().getLocation())){
                game.onVoidDamage(gp.get());
                continue;
            }
            Location l = gp.get().getLocation().clone().add(0,-1,0);
            if(l.getBlock().getType() != Material.AIR){
                cache.add(l);
            }

        }
    }
}
