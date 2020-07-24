package me.lightlord323dev.enhancedchests.api;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Luda on 7/24/2020.
 */
public abstract class NBTUtil {

    public abstract ItemStack setInt(ItemStack itemStack, String key, int value);

    public abstract ItemStack setBoolean(ItemStack itemStack, String key, boolean value);

    public abstract ItemStack setString(ItemStack itemStack, String key, String value);

    public abstract int getInt(ItemStack itemStack, String key);

    public abstract String getString(ItemStack itemStack, String key);

    public abstract boolean hasKey(ItemStack itemStack, String key);

}
