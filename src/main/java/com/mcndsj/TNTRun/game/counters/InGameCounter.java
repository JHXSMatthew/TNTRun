package com.mcndsj.TNTRun.game.counters;

import com.mcndsj.TNTRun.Core;
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
public class InGameCounter extends BukkitRunnable implements TimeGetable {


    private Game game;
    private List<Location> cache;
    private int count;

    public InGameCounter(Game game) {
        this.game = game;
        cache = new ArrayList<>();

        new BukkitRunnable(){
            int innter = 10;
            @Override
            public void run() {
                innter --;
                if(innter <= 0){
                    game.sendSound(Sound.LEVEL_UP);
                    game.sendTitle(null, ChatColor.RED + "跑酷开始!");
                    start();
                    this.cancel();
                }else{
                    game.sendSound(Sound.DIG_WOOD);
                    game.sendTitle(null, ChatColor.RED + "跑酷将在 " + ChatColor.GREEN + innter +  ChatColor.RED + " 秒后正式开始");
                }


            }
        }.runTaskTimer(Core.get(),0,20);



    }

    private void start(){
        runTaskTimer(Core.get(), 0, 4);
    }


    public void run() {
        count ++;
        for (Location l : cache) {
            l.getWorld().playSound(l, Sound.DIG_SAND, 1F, 1F);
            l.getBlock().setType(Material.AIR);
            l.add(0, -1, 0).getBlock().setType(Material.AIR);
        }

        cache.clear();

        for (GamePlayer gp : game.getInGame()) {
            if (gp.get().getGameMode() == GameMode.SPECTATOR)
                continue;

            if (game.getMap().rangeCheck(gp.get().getLocation())) {
                game.onVoidDamage(gp.get());
                continue;
            }
            /*
            Location l = gp.get().getLocation().clone().add(0, -1, 0);
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
            */
            Block b = gp.getBlockUnderPlayer();
            if(b == null){
                b = gp.get().getLocation().clone().add(0, -1, 0).getBlock();
            }
            if(b.getType() == Material.WATER){
                game.onVoidDamage(gp.get());
                continue;
            }
            if(b.getType() != Material.AIR){
                cache.add(b.getLocation());
            }
        }
        if(count % 5 == 0)
            game.getBoard().updateCounter();


    }

    @Override
    public int getTime() {
        return count/5;
    }
}
