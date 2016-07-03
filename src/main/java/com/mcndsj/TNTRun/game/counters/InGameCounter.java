package com.mcndsj.TNTRun.game.counters;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 19/06/2016.
 */
public class InGameCounter extends BukkitRunnable {



    private Game game;
    private List<Location> cache;

    public InGameCounter(Game game){
        this.game = game;
        cache = new ArrayList<>();
        runTaskTimer(Core.get(),0,20);

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
            List<Location> list = new ArrayList<>();
            double shiftX = Math.abs(l.getX() - l.getBlockX());
            double shiftZ = Math.abs(l.getZ() - l.getBlockZ());
            list.add(l);
            //-0.3~0.3, 0.3~0.7
            if (shiftX <= 0.3) list.add(l.clone().add(-1, 0, 0));
            if (shiftX >= 0.7) list.add(l.clone().add(1, 0, 0));
            if (shiftZ <= 0.3) list.add(l.clone().add(0, 0, -1));
            if (shiftZ >= 0.7) list.add(l.clone().add(0, 0, 1));
            list.forEach((e) -> {
                if (e.getBlock().getType() != Material.AIR) cache.add(e);
            });
        }

        game.getWatchdog().feed(); // 喂狗
    }
}
