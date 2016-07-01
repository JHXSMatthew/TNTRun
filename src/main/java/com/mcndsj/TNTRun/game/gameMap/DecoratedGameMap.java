package com.mcndsj.TNTRun.game.gameMap;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.manager.ThreadManager;
import com.mcndsj.TNTRun.utils.LocationFactory;
import com.mcndsj.TNTRun.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

/**
 * Created by Matthew on 1/07/2016.
 */
public class DecoratedGameMap implements IGameMap{


    private GameMap map;
    private int id = 0;

    String worldName;
    LocationFactory up;
    LocationFactory down;
    LocationFactory spawn;

    public DecoratedGameMap(int id, GameMap map){
        this.map = map;
        this.id = id;

        worldName = map.getWorldName()+ "_" + id;
        up = map.getUpCornerF().clone().setWorldO(getWorldName());
        down = map.getDownCornerF().clone().setWorldO(getWorldName());
        spawn = map.getSpawnF().clone().setWorldO(getWorldName());
    }


    @Override
    public String getName() {
        return map.getName();
    }

    @Override
    public World getWorld() {
        return map.getWorld();
    }

    @Override
    public Location getLobby() {
        return map.getLobby();
    }

    @Override
    public Location getSpawn() {
        return spawn.getLocation();
    }

    @Override
    public Location getUpCorner() {
        return up.getLocation();
    }

    @Override
    public Location getDownCorner() {
        return down.getLocation();
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public boolean rangeCheck(Location l) {
        return map.rangeCheck(l);
    }

    @Override
    public void load() {
        ThreadManager.runTask(new Runnable() {
            @Override
            public void run() {
                WorldUtils.copyWorld(new File(Core.get().getDataFolder().getPath() + File.separator + Config.mapFolderName + File.separator + map.getWorldName()) , new File(worldName));
                //callback
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        WorldUtils.loadWorld(worldName);
                    }
                }.runTask(Core.get());

            }
        });
    }

    @Override
    public void unload() {
        ThreadManager.runTask(new Runnable() {
            @Override
            public void run() {
                WorldUtils.deleteWorld(worldName);
            }
        });
    }
}
