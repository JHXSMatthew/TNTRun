package com.mcndsj.TNTRun.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.game.UsedI.IReceiver;
import com.mcndsj.TNTRun.utils.BungeeUtils;
import com.mcndsj.TNTRun.utils.NMSHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
@Setter
public class GamePlayer implements IReceiver{
    private UUID uuid;
    private String name;
    private Player player;
    private Game game;

    private boolean isUsingPlayerHider = false;
    private long lastUsePlayerHider = -1;

    private long lastChat = -1;
    private List<String> chatQueue;



    public GamePlayer(UUID uuid,String name){
        this.uuid = uuid;
        this.name = name;

    }

    public void ConstructCall(Player p){
        this.player = p;
        chatQueue = new ArrayList<String>();
    }

    public String getPrefix(){
        return Core.chat.getPlayerPrefix(player);
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
                if(p != this.player){
                    this.player.showPlayer(p);
                }
            }
        }else{
            isUsingPlayerHider = true;
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p != this.player){
                    this.player.hidePlayer(p);
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
        return player;
    }


    public void sendMessage(String msg) {
        player.sendMessage(ChatColor.AQUA+ "游乐园 >> " + ChatColor.RESET + msg);
    }

    public void sendTitle(String message, String sub){
        if(message == null){
            NMSHandler.get().sendTitle(player,0,40,20," ",sub);
        }else if(sub == null){
            NMSHandler.get().sendTitle(player,0,40,20,message," ");
        }else {
            NMSHandler.get().sendTitle(player,0,40,20,message,sub);
        }
    }

    public void sendActionBar(String bar) {
        NMSHandler.get().sendActionBar(player,bar);

    }

    public void sendSound(Sound s) {
        player.playSound(player.getLocation(),s,1F,1F);
    }

    public void teleport(Location l) {
        player.teleport(l);
    }

    public void setPreGameStart() {
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());

    }

    public void setPostJoin() {
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());
    }


    public void setPostFinish(){
        this.game = null;
    }

    public void sendToLobby() {
        player.kickPlayer("GameFinished,Kick");
        //TODO: bungee support
        //BungeeUtils.sendPlayerTo(player);
    }

    public void hide(GamePlayer gp){
        hide(gp.get());
    }

    public void show(GamePlayer gp){
        show(gp.get());
    }


    private void hide(Player p){
        player.hidePlayer(p);
        p.hidePlayer(player);
    }

    private void show(Player p){
        player.showPlayer(p);
        p.showPlayer(p);
    }

    public Block getBlockUnderPlayer() {
        Location location = player.getLocation();
        Block returnValue = location.getWorld().getBlockAt((int)(location.getX() + Config.player_bound) ,location.getBlockY() - 1 ,(int)(location.getZ() -Config.player_bound));
        if (returnValue.getType() != Material.AIR) {
            return returnValue;
        }
        returnValue = location.getWorld().getBlockAt((int)(location.getX() - Config.player_bound) ,location.getBlockY() - 1 ,(int)(location.getZ() +Config.player_bound));

        if (returnValue.getType() != Material.AIR) {
            return returnValue;
        }
        returnValue = location.getWorld().getBlockAt((int)(location.getX() + Config.player_bound) ,location.getBlockY() - 1 ,(int)(location.getZ() +Config.player_bound));
        if (returnValue.getType() != Material.AIR) {
            return returnValue;
        }
        returnValue = location.getWorld().getBlockAt((int)(location.getX() - Config.player_bound) ,location.getBlockY() - 1,(int)(location.getZ() -Config.player_bound));
        if (returnValue.getType() != Material.AIR) {
            return returnValue;
        }
        return null;
    }

}
