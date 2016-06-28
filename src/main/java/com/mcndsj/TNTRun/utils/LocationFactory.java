package com.mcndsj.TNTRun.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemFactory;
import org.json.simple.JSONObject;

/**
 * Created by Matthew on 29/06/2016.
 */
public class LocationFactory {

    private int x;
    private int y;
    private int z;
    private String world;

    public LocationFactory(int x, int y , int z , String world){
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public LocationFactory(JSONObject obj){
        this.x = Integer.parseInt((String) obj.get("x"));
        this.z = Integer.parseInt((String) obj.get("z"));
        this.y = Integer.parseInt((String) obj.get("y"));
        this.world = (String) obj.get("world");
    }

    public LocationFactory(Location l){
        this.x  = l.getBlockX();
        this.z = l.getBlockZ();
        this.y = l.getBlockY();
        this.world = l.getWorld().getName();
    }

    public Location getLocation (){
        return Bukkit.getWorld(world).getBlockAt(x,y,z).getLocation();
    }

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("x",x);
        obj.put("y",y);
        obj.put("z",z);
        obj.put("world",world);
        return obj;
    }


}
