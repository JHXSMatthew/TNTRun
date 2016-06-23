package com.mcndsj.TNTRun.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mcndsj.TNTRun.Core;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 19/06/2016.
 */
public class BungeeUtils {

    public static void sendPlayerTo(Player p){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(p.getName());
        p.sendPluginMessage(Core.get(), "LobbyConnect", out.toByteArray());
    }
}
