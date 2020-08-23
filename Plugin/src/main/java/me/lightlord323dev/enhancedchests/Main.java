package me.lightlord323dev.enhancedchests;

import me.lightlord323dev.enhancedchests.api.NBTUtil;
import me.lightlord323dev.enhancedchests.api.manager.ManagerHandler;
import me.lightlord323dev.enhancedchests.command.ECCommand;
import me.lightlord323dev.enhancedchests.v1_13_R2.NBTUtil_1_13_R2;
import me.lightlord323dev.enhancedchests.v1_14_R1.NBTUtil_1_14_R1;
import me.lightlord323dev.enhancedchests.v1_15_R1.NBTUtil_1_15_R1;
import me.lightlord323dev.enhancedchests.v1_16_R1.NBTUtil_1_16_R1;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

/**
 * Created by Luda on 7/15/2020.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    private ManagerHandler managerHandler;

    // nbt util
    private NBTUtil nbtUtil;

    @Override
    public void onEnable() {
        instance = this; // main instance

        // initialize nbtutil
        if (!initNbtUtil()) {
            getLogger().severe("Could not find a supported version, disabling plugin...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        // commands
        getCommand("enhancedchests").setExecutor(new ECCommand());

        //files
        initFiles();

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

    public NBTUtil getNbtUtil() {
        return nbtUtil;
    }

    private void initFiles() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
    }

    private boolean initNbtUtil() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split(Pattern.quote("."))[3];
        switch (version) {
            case "v1_16_R1":
                nbtUtil = new NBTUtil_1_16_R1();
                return true;
            case "v1_15_R1":
                nbtUtil = new NBTUtil_1_15_R1();
                return true;
            case "v1_14_R1":
                nbtUtil = new NBTUtil_1_14_R1();
                return true;
            case "v1_13_R2":
                nbtUtil = new NBTUtil_1_13_R2();
                return true;
            default:
                return false;
        }
    }
}
