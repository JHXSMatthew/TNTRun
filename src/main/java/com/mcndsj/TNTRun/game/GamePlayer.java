package com.mcndsj.TNTRun.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.game.UsedI.IReceiver;
import com.mcndsj.TNTRun.utils.BungeeUtils;
import com.mcndsj.TNTRun.utils.NMSHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;


public class GamePlayer implements IReceiver {
    private UUID uuid;
    private String name;
    private Player p ;

    private boolean isUsingPlayerHider = false;
    private long lastUsePlayerHider = -1;

    private long lastChat = -1;
    private List<String> chatQueue;



    public GamePlayer(UUID uuid,String name){
        this.uuid = uuid;
        this.name = name;

    }

    public void ConstructCall(Player p){
        this.p = p;
        chatQueue = new ArrayList<String>();
    }

    public String getPrefix(){
        return Core.chat.getPlayerPrefix(p);
    }

    public long getLastChat(){
        return lastChat;
    }


    public long getLastUsePlayerHider(){
        return lastUsePlayerHider;
    }
    public void setHider(){
        if(isUsingPlayerHider){
            isUsingPlayerHider = false;
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p != this.p){
                    this.p.showPlayer(p);
                }
            }
        }else{
            isUsingPlayerHider = true;
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p != this.p){
                    this.p.hidePlayer(p);
                }
            }
        }
        this.lastUsePlayerHider = System.currentTimeMillis() + 1000 * 3;
    }

    public boolean isUsingPlayerHider(){
        return this.isUsingPlayerHider;
    }

    public boolean isChatAllow(String str){
        for(String s : chatQueue){
            if(str.equals(s) ||
                    str.substring(0, str.length() - 2) .equals(s)){
                return false;
            }
        }
        return true;
    }

    public String getName(){
        return this.name;
    }

    public UUID getUUid(){
        return this.uuid;
    }

    public void setLastChat(String str, long time){
        this.lastChat = time;
        if(chatQueue.size() > 3){
            chatQueue.remove(0);
            chatQueue.add(str);
        }
    }

    public Player get(){
        return p;
    }


    public void sendMessage(String msg) {
        p.sendMessage(ChatColor.AQUA+ "游乐园 >>" + ChatColor.RESET + msg);
    }

    public void sendTitle(String message, String sub){
        if(message == null){
            NMSHandler.get().sendTitle(p,0,40,20," ",sub);
        }else if(sub == null){
            NMSHandler.get().sendTitle(p,0,40,20,message," ");
        }else {
            NMSHandler.get().sendTitle(p,0,40,20,message,sub);
        }
    }

    public void sendActionBar(String bar) {
        NMSHandler.get().sendActionBar(p,bar);

    }

    public void sendSound(Sound s) {
        p.playSound(p.getLocation(),s,1F,1F);
    }

    public void teleport(Location l) {
        p.teleport(l);
    }

    public void setPreGameStart() {
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(p.getMaxHealth());

    }

    public void setPostJoin() {
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(p.getMaxHealth());
    }

    public void sendToLobby() {
        BungeeUtils.sendPlayerTo(p);
    }


}
