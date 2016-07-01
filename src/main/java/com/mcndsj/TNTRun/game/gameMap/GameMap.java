package com.mcndsj.TNTRun.game.gameMap;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.manager.ThreadManager;
import com.mcndsj.TNTRun.utils.FileUtils;
import com.mcndsj.TNTRun.utils.LocationFactory;
import com.mcndsj.TNTRun.utils.WorldUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Matthew on 19/06/2016.
 */
@Setter
public class GameMap implements IGameMap{

    private String name;
    @Getter
    private String worldName;

    private LocationFactory upCorner;
    private LocationFactory downCorner;
    private LocationFactory spawn;

    /**
     * on loading
     * @param f
     */
    public GameMap(File f){
        String s = null;
        try {
            s = FileUtils.readFile(f.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Cannot load path " + f.getAbsolutePath());
            e.printStackTrace();
            return;
        }

        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(s);
            for(Field field : GameMap.class.getDeclaredFields()){
                if(field.getType() == String.class
                        ||field.getType() == int.class ) {
                    field.setAccessible(true);
                    Object temp = obj.get(field.getName());
                    field.set(this, temp);
                }
            }
            upCorner = new LocationFactory((JSONObject) obj.get("upCorner"));
            downCorner = new LocationFactory((JSONObject) obj.get("downCorner"));
            spawn = new LocationFactory((JSONObject) obj.get("spawn"));
            //name = (String) obj.get("name");    //2016-6-30 [NOTE] Added as new feature -- Mulan Lin , reflection done the job for all strings.
        } catch (ParseException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    public GameMap(LocationFactory up,LocationFactory down, LocationFactory spawn, String worldName,String name){
        this.name = name;
        this.worldName = worldName;
        this.upCorner = up;
        this.downCorner = down;
        this.spawn = spawn;
    }

    /**
     *  on creation
     * @param p
     */
    public GameMap(Player p){
        worldName = p.getWorld().getName();
    }

    public void load(){
        ThreadManager.runTask(new Runnable() {
            @Override
            public void run() {
                WorldUtils.copyWorld(new File(Core.get().getDataFolder().getPath() + File.separator + Config.mapFolderName+ File.separator + worldName) , new File(worldName));
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

    public void unload(){
        ThreadManager.runTask(new Runnable() {
            @Override
            public void run() {
                WorldUtils.deleteWorld(worldName);
            }
        });
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld(){
        return Bukkit.getWorld(worldName);
    }

    public Location getLobby(){
        return Bukkit.getWorld("lobby").getSpawnLocation();
    }

    public Location getSpawn(){
        return spawn.getLocation();
    }

    public void setSpawn(LocationFactory spawn) {
        this.spawn = spawn;
    }

    public Location getUpCorner(){
        return upCorner.getLocation();
    }

    public Location getDownCorner(){
        return downCorner.getLocation();
    }

    /**
     *
     * @param l the current location
     * @return true if out of range, false if still in the range
     */
    public boolean rangeCheck(Location l){
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        return x > upCorner.getX() || y > upCorner.getY() || z > upCorner.getZ() || x < downCorner.getX() || y < downCorner.getY() || z < downCorner.getZ();
    }

    public void addBounds(Location l){
        if(upCorner != null){
            downCorner = new LocationFactory(l);
            return;
        }
        upCorner = new LocationFactory(l);
    }

    public void save(){
        File f = new File(new File(Core.get().getDataFolder().getPath(),Config.configFolderName), worldName);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONObject json = new JSONObject();
        try {
            for(Field field : GameMap.class.getDeclaredFields()){
                if(field.getType() == String.class
                        ||field.getType() == int.class ) {
                    field.setAccessible(true);
                    Object toSave = field.get(this);
                    json.put(field.getName(),toSave);
                }
            }

            int[] up = new int[3];
            int[] down = new int[3];

            up[0] = upCorner.getX() > downCorner.getX() ? upCorner.getY() : downCorner.getX();
            up[1] = upCorner.getY() > downCorner.getY() ? upCorner.getY() : downCorner.getY();
            up[2] = upCorner.getZ() > downCorner.getZ() ? upCorner.getZ() : downCorner.getZ();

            down[0] = upCorner.getX() < downCorner.getX() ? upCorner.getY() : downCorner.getX();
            down[1] = upCorner.getY() < downCorner.getY() ? upCorner.getY() : downCorner.getY();
            down[2] = upCorner.getZ() < downCorner.getZ() ? upCorner.getZ() : downCorner.getZ();


            json.put("upCorner",new LocationFactory(up[0],up[1],up[2],upCorner.getWorld()).getJSONObject());
            json.put("downCorner",new LocationFactory(down[0],down[1],down[2],upCorner.getWorld()).getJSONObject());
            json.put("spawn",spawn.getJSONObject()) ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.writeFile(f.getPath(),json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        WorldUtils.copyWorld(new File(worldName), new File(Core.get().getDataFolder() + File.separator + Config.mapFolderName, worldName));
    }


    protected LocationFactory getUpCornerF(){
        return this.upCorner;
    }
    protected LocationFactory getDownCornerF(){
        return this.downCorner;
    }
    protected LocationFactory getSpawnF(){
        return this.spawn;
    }

    @Override
    public GameMap clone(){
        return new GameMap(upCorner.clone(),downCorner.clone(),spawn.clone(), worldName,name);
    }
}

