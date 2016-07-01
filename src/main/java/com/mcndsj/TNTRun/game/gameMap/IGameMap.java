package com.mcndsj.TNTRun.game.gameMap;

import com.mcndsj.TNTRun.game.Game;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Matthew on 1/07/2016.
 */
public interface IGameMap {
    String getName();

    World getWorld();

    Location getLobby();

    Location getSpawn();

    Location getUpCorner();

    Location getDownCorner();

    String getWorldName();

    boolean rangeCheck(Location l);

    void load();

    void unload();




}
