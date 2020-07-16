package me.lightlord323dev.enhancedchests;

import me.lightlord323dev.enhancedchests.api.manager.ManagerHandler;
import me.lightlord323dev.enhancedchests.command.ECCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Luda on 7/15/2020.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    private ManagerHandler managerHandler;

    @Override
    public void onEnable() {
        instance = this; // main instance

        // commands
        getCommand("enhancedchests").setExecutor(new ECCommand());

        // handler
        managerHandler = new ManagerHandler();
        managerHandler.loadManagers();
    }

    @Override
    public void onDisable() {
        managerHandler.unloadManagers();
    }

    public static Main getInstance() {
        return instance;
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}
