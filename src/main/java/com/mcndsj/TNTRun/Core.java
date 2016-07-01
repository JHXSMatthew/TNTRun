package com.mcndsj.TNTRun;

import com.mcndsj.TNTRun.commands.tntCommand;
import com.mcndsj.TNTRun.listeners.PlayerListener;
import com.mcndsj.TNTRun.manager.GameManager;
import com.mcndsj.TNTRun.manager.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Matthew on 19/06/2016.
 */
public class Core extends JavaPlugin {

    public static Chat chat = null;
    private static Core instance;


    public void onEnable(){
        instance = this;
        saveDefaultConfig();

        getServer().getPluginCommand("tnt").setExecutor(new tntCommand());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "LobbyConnect");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        GameManager.get().nextNewGame();
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    public static Core get(){
        return instance;
    }
}
