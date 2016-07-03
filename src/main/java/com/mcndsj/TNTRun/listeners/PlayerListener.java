package com.mcndsj.TNTRun.listeners;

import com.mcndsj.TNTRun.config.Config;
import com.mcndsj.TNTRun.config.Messages;
import com.mcndsj.TNTRun.game.Game;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
import com.mcndsj.TNTRun.manager.GameManager;
import com.mcndsj.TNTRun.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;


public class PlayerListener implements Listener{


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreJoin(PlayerLoginEvent evt){
        if(evt.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        if(Config.setUp)
            return;

        PlayerManager.get().createControlPlayer(evt.getPlayer().getUniqueId(), evt.getPlayer().getName());
        //load data
    }

    @EventHandler
    public void onDamage(EntityDamageEvent evt){
        if (/*evt.getCause() == EntityDamageEvent.DamageCause.VOID && */evt.getEntity() instanceof Player) {
            Game g = PlayerManager.get().getControlPlayer(evt.getEntity().getName()).getGame();
            if(g != null && g.getGameState() == GameState.inGaming){
                g.onVoidDamage((Player) evt.getEntity());
            }
        }
        evt.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent evt){
        evt.setQuitMessage(null);

        if(Config.setUp)
            return;

        // Before the player is removed, quit game operation executes in current game, or receive a NullPointerException.
        GameManager.get().gameQuit(evt.getPlayer());
        // Then remove it.
        PlayerManager.get().removeControlPlayer(evt.getPlayer().getName());

        /*new BukkitRunnable(){
            public void run() {
                if(Bukkit.getServer().getOnlinePlayers().size() == 0){
                    Bukkit.shutdown();
                }
            }
        }.runTaskLater(Core.get(),20);
           */ // [DEBUG] 2016-7-1 Try out memory leak

    }

    @EventHandler
    public void handleItemPickUp(PlayerPickupItemEvent evt){
        evt.getItem().remove();
        evt.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void noItemMove (InventoryClickEvent evt){
        if(evt.getView().getPlayer() instanceof Player){
            if(!evt.getView().getPlayer().hasPermission("lobby.admin")){
                evt.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent evt){
        if(evt.getRightClicked().getType() == EntityType.ITEM_FRAME){
            if(!evt.getPlayer().hasPermission("lobby.admin")){
                evt.setCancelled(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractEvent(PlayerInteractEvent evt){
        if(!evt.getPlayer().hasPermission("lobby.admin")){
            evt.setCancelled(true);
        }
    }



    @EventHandler
    public void noHunger(FoodLevelChangeEvent evt){
        evt.setCancelled(true);
    }
    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent evt){
        evt.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent evt){
        evt.setJoinMessage(null);
        if(Config.setUp)
            return;

        Player p = evt.getPlayer();
        GamePlayer gp = PlayerManager.get().getControlPlayer(evt.getPlayer().getName());
        gp.ConstructCall(p);
        GameManager.get().hide(gp);
        GameManager.get().gameJoin(evt.getPlayer());

    }


    //ChatLimit
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatEvent(AsyncPlayerChatEvent evt){
        Player p = evt.getPlayer();
        GamePlayer cp =  PlayerManager.get().getControlPlayer(p.getName());
        if(cp.getLastChat() > System.currentTimeMillis()){
                p.sendMessage(ChatColor.YELLOW +"距离您上次发言还不足3秒,为了 避免刷屏嫌疑,请您稍后再试!");
                evt.setCancelled(true);
                return;
            }
            if(!cp.isChatAllow(evt.getMessage())){
                p.sendMessage(ChatColor.YELLOW +"您的近几次发言具有重复性,请勿重复刷屏!");
                evt.setCancelled(true);
                return;
            }


        if(evt.getMessage().equals("1")){
            p.sendMessage(Messages.NotifyPrefix + "您没有掉线...");
            evt.setCancelled(true);
            return;
        }

        cp.setLastChat(evt.getMessage(), System.currentTimeMillis() + 4000);
        evt.setFormat("0 " + ChatColor.translateAlternateColorCodes('&', cp.getPrefix().replace("&l", ""))  + "%s "+ ChatColor.GOLD + ">>"+ ChatColor.GRAY + " %s");
    }


}
