package com.mcndsj.TNTRun.game.UsedI;

import org.bukkit.Location;
import org.bukkit.Sound;

/**
 * Created by Matthew on 19/06/2016.
 */
public interface IReceiver {

    void sendMessage(String msg);

    void sendTitle(String message, String sub);

    void sendActionBar(String bar);

    void sendSound(Sound s);

    void teleport(Location l);

    void setPreGameStart();

    void setPostJoin();

    void sendToLobby();

}
