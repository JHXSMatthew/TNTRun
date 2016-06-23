package com.mcndsj.TNTRun.listeners;

import com.mcndsj.TNTRun.Core;
import com.mcndsj.TNTRun.config.Messages;
import com.mcndsj.TNTRun.game.GamePlayer;
import com.mcndsj.TNTRun.game.GameState;
import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerListener implements Listener{


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreJoin(PlayerLoginEvent evt){
        if(evt.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        Core.getPlayerManager().createControlPlayer(evt.getPlayer().getUniqueId(), evt.getPlayer().getName());
        //load data
    }

    @EventHandler
    public void onDamage(EntityDamageEvent evt){
        if(evt.getCause() == EntityDamageEvent.DamageCause.VOID && evt.getEntity() instanceof Player){
            if(Core.getGameManager().currentGame.getGameState() == GameState.inGaming){
                Core.getGameManager().currentGame.onVoidDamage((Player) evt.getEntity());
            }
        }
        evt.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent evt){
        evt.setQuitMessage("");
        Core.getPlayerManager().removeControlPlayer(evt.getPlayer().getName());
        Core.getGameManager().currentGame.gameQuit(evt.getPlayer());

        new BukkitRunnable(){
            public void run() {
                if(Bukkit.getServer().getOnlinePlayers().size() == 0){
                    Bukkit.shutdown();
                }
            }
        }.runTaskLater(Core.get(),20);


    }

    @EventHandler
    public void handleItemPickUp(PlayerPickupItemEvent evt){
        evt.getItem().remove();
        evt.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void noItemMove (InventoryClickEvent evt){
        if(evt.getView().getPlayer() instanceof Player){
            if(!((Player)evt.getView().getPlayer()).hasPermission("lobby.admin")){
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


    @EventHandler
    public void onLogin(PlayerLoginEvent evt){
        if(evt.getResult() != PlayerLoginEvent.Result.ALLOWED){
            if(evt.getPlayer().hasPermission("lobby.fulljoin")){
                evt.allow();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent evt){
        Player p = evt.getPlayer();
        Core.getPlayerManager().getControlPlayer(evt.getPlayer().getName()).ConstructCall(p);

        Core.getGameManager().currentGame.gameJoin(evt.getPlayer());

    }


    //ChatLimit
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatEvent(AsyncPlayerChatEvent evt){
        Player p = evt.getPlayer();
        GamePlayer cp = Core.getPlayerManager().getControlPlayer(p.getName());
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
