package com.mcndsj.TNTRun.manager;

import com.mcndsj.TNTRun.game.GamePlayer;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
	private ConcurrentHashMap<String,GamePlayer> hash = new ConcurrentHashMap<String,GamePlayer>();
	

	public GamePlayer getControlPlayer(String name){
		GamePlayer lp = hash.get(name);
	
		return lp != null? lp : createControlPlayer(Bukkit.getPlayer(name).getUniqueId(),name);
	}
	
	
	public GamePlayer createControlPlayer(UUID uuid , String name){
		GamePlayer lp = new GamePlayer(uuid,name);
		hash.put(name, lp);
		return lp;
	}
	
	public void removeControlPlayer(String name){
		hash.remove(name);
	}
	

}
