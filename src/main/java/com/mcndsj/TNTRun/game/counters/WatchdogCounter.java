package com.mcndsj.TNTRun.game.counters;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.game.Game;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The watch dog is used for monitoring all games that in errors, such ones have 0 player or so on.
 * Updating actions like player joining, player killing, are regarded as "feed dog" keeps this dog sleepy.
 * Once in 60 seconds without being feed, this watch dog will kill the game in order to save RAM and resource.
 * Watch dog is relied on BukkitRunnable at present.
 *  -- Mulan Lin
 *
 * Created by Mulan Lin('Snake1999') on 2016/7/3 9:43.
 */
public class WatchdogCounter extends BukkitRunnable {

    private Game game;
    private long lastUpdateMillis = 0;
    private HungryProcedure onHungry = () -> {};
    private boolean alive = false;

    @FunctionalInterface
    public interface HungryProcedure {
        void onHungry();
    }

    public WatchdogCounter(Game game, HungryProcedure procedure){
        this.game = game;
        this.onHungry = procedure;
        runTaskTimer(Core.get(), 0, 20*10); //10 seconds * 20 tps
    }

    public final void feed() {
        lastUpdateMillis = System.currentTimeMillis();
    }

    // 决定就是你了，出来吧，看门狗
    public final void summon() {
        this.feed();
        this.alive = true;
    }

    @Override
    public void run() {
        if (!this.alive) return;
        if ((System.currentTimeMillis() - lastUpdateMillis)<= 60 * 1000) return; //60 sec * 1000 millis/sec
        onHungry.onHungry();
    }

    public Game getGame() {
        return game;
    }

}
