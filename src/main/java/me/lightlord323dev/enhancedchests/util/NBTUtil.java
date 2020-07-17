package me.lightlord323dev.enhancedchests.util;

import net.minecraft.server.v1_16_R1.ItemStack;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;

/**
 * Created by Luda on 7/16/2020.
 */
public class NBTUtil {

    private ItemStack itemStack;

    public NBTUtil(org.bukkit.inventory.ItemStack itemStack) {
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!this.itemStack.hasTag())
            this.itemStack.setTag(new NBTTagCompound());
    }

    public NBTUtil setInt(String key, int value) {
        itemStack.getTag().setInt(key, value);
        return this;
    }

    public NBTUtil setBoolean(String key, boolean value) {
        itemStack.getTag().setBoolean(key, value);
        return this;
    }

    public NBTUtil setString(String key, String value) {
        itemStack.getTag().setString(key, value);
        return this;
    }

    public int getInt(String key) {
        return itemStack.getTag().getInt(key);
    }

    public String getString(String key) {
        return itemStack.getTag().getString(key);
    }

    public boolean hasKey(String key) {
        return itemStack.getTag().hasKey(key);
    }

    public org.bukkit.inventory.ItemStack getItemStack() {
        return CraftItemStack.asBukkitCopy(itemStack);
    }

}
