package me.lightlord323dev.enhancedchests.api.echest;

import java.util.UUID;

/**
 * Created by Luda on 7/15/2020.
 */
public class EnchancedChest {

    private UUID owner;
    private int size;
    private String world;
    private int[] location;

    public EnchancedChest(UUID owner, int size, String world, int[] location) {
        this.owner = owner;
        this.size = size;
        this.world = world;
        this.location = location;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getSize() {
        return size;
    }

    public String getWorld() {
        return world;
    }

    public int[] getLocation() {
        return location;
    }
}
