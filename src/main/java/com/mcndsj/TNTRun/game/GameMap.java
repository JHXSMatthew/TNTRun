package com.mcndsj.TNTRun.game;

import com.mcndsj.TNTRun.utils.FileUtils;
import com.mcndsj.TNTRun.utils.LocationFactory;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
public class GameMap {

    private String name;
    private String wordName;
    private String displayName;

    private LocationFactory upCorner;
    private LocationFactory downCorner;
    private LocationFactory spawn;

    /**
     * on loading
     * @param path
     */
    public GameMap(String path){
        File f = new File(path);
        String s = null;
        try {
            s = FileUtils.readFile(path);
        } catch (IOException e) {
            System.err.println("Cannot load path " + path);
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
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *  on creation
     * @param p
     */
    public GameMap(Player p){

    }

    public void re_gen(){

    }

    public void load(){

    }

    public void unload(){

    }

    public String getName(){
        return name;
    }

    //TODO: finish all those
    public World getWorld(){
        return null;
    }

    public Location getLobby(){
        return null;
    }

    public Location getSpawn(){
        return null;
    }
}
