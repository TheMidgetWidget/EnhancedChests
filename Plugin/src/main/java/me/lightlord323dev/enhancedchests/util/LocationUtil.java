package me.lightlord323dev.enhancedchests.util;

import org.bukkit.Location;

/**
 * Created by Luda on 7/17/2020.
 */
public class LocationUtil {

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

    public static String serializeLocation(String world, int[] location) {
        return world + ";" + location[0] + ";" + location[1] + ";" + location[2];
    }

    public static int[] deserializeLocation(String location) {
        int[] loc = new int[3];
        String[] data = location.split(";");
        loc[0] = Integer.parseInt(data[1]);
        loc[1] = Integer.parseInt(data[2]);
        loc[2] = Integer.parseInt(data[3]);
        return loc;
    }

    public static String getWorldName(String location) {
        String[] data = location.split(";");
        return data[0];
    }

}
