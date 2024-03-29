package com.mcndsj.TNTRun.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemFactory;
import org.json.simple.JSONObject;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

/**
 * Created by Matthew on 29/06/2016.
 */
@Getter
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
        this.x = Integer.parseInt(String.valueOf(obj.get("x")));
        this.z = Integer.parseInt(String.valueOf(obj.get("z")));
        this.y = Integer.parseInt(String.valueOf(obj.get("y")));
        this.world = String.valueOf(obj.get("world"));
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

    /**
     *
     * @param w world name
     * @return the original , not clone
     */
    public LocationFactory setWorldO(String w){
        world = w;
        return this;
    }

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("x",x);
        obj.put("y",y);
        obj.put("z",z);
        obj.put("world",world);
        return obj;
    }

    @Override
    public LocationFactory clone(){
        return new LocationFactory(x,y,z,world);
    }
}
