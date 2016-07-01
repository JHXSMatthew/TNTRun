package com.mcndsj.TNTRun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Matthew on 1/07/2016.
 */
public class BlockListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent evt){
        evt.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent evt) {
        evt.setCancelled(true);
    }
}
