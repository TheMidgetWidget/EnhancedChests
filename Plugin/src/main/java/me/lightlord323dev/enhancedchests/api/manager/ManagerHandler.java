package me.lightlord323dev.enhancedchests.api.manager;

import me.lightlord323dev.enhancedchests.Main;
import me.lightlord323dev.enhancedchests.manager.EnhancedChestManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 7/15/2020.
 */
public class ManagerHandler {

    // Register all managers here

    private EnhancedChestManager enhancedChestManager;

    private List<Manager> managers;

    public void loadManagers() {
        managers = new ArrayList<>();
        managers.add(enhancedChestManager = new EnhancedChestManager());
        managers.forEach(manager -> {
            manager.onLoad();
            if (manager instanceof Listener)
                Bukkit.getPluginManager().registerEvents((Listener) manager, Main.getInstance());
        });
    }

    public void unloadManagers() {
        managers.forEach(Manager::onUnload);
    }

    public EnhancedChestManager getEnhancedChestManager() {
        return enhancedChestManager;
    }
}
