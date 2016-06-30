package com.mcndsj.TNTRun.game;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
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
import org.bukkit.util.FileUtil;
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
public class GameMap {

    private String name;
    private String wordName;
    private String displayName;  //// TODO: 2016/6/30 [FUTURE] Unused variable, maybe new function? --Mulan Lin

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
            name = (String) obj.get("name");    //2016-6-30 [NOTE] Added as new feature -- Mulan Lin
        } catch (ParseException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     *  on creation
     * @param p
     */
    public GameMap(Player p){
        wordName = p.getWorld().getName();
    }

    public void re_gen(){
        unload();
        load();
    }

    public void load(){
        WorldUtils.copyWorld(new File(Core.get().getDataFolder().getPath() + File.pathSeparator + "arena" + File.pathSeparator + wordName) , new File(wordName));
        WorldUtils.loadWorld(wordName);
    }

    public void unload(){
        WorldUtils.deleteWorld(wordName);
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld(){
        return Bukkit.getWorld(wordName);
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
        if(x > upCorner.getX() || y > upCorner.getY() || z > upCorner.getZ() || x < downCorner.getX() || y < downCorner.getY() || z < downCorner.getZ()){
            return true;
        }
        return false;
    }

    public void addBounds(Location l){
        if(upCorner != null){
            downCorner = new LocationFactory(l);
            return;
        }
        upCorner = new LocationFactory(l);
    }

    public void save(){
        File f = new File(new File(Core.get().getDataFolder().getPath(),Config.configFolderName),wordName);
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
            json.put("upCorner",upCorner.getJSONObject());
            json.put("downCorner",downCorner.getJSONObject());
            json.put("spawn",spawn.getJSONObject()) ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.writeFile(f.getPath(),json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

