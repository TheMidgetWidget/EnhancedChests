package me.lightlord323dev.enhancedchests.v1_16_R1;

import me.lightlord323dev.enhancedchests.api.NBTUtil;
import net.minecraft.server.v1_16_R1.ItemStack;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;

/**
 * Created by Luda on 7/24/2020.
 */
public class NBTUtil_1_16_R1 extends NBTUtil {

    public org.bukkit.inventory.ItemStack setInt(org.bukkit.inventory.ItemStack itemStack, String key, int value) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag())
            nmsItemStack.setTag(new NBTTagCompound());
        nmsItemStack.getTag().setInt(key, value);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    public org.bukkit.inventory.ItemStack setBoolean(org.bukkit.inventory.ItemStack itemStack, String key, boolean value) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag())
            nmsItemStack.setTag(new NBTTagCompound());
        nmsItemStack.getTag().setBoolean(key, value);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    public org.bukkit.inventory.ItemStack setString(org.bukkit.inventory.ItemStack itemStack, String key, String value) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag())
            nmsItemStack.setTag(new NBTTagCompound());
        nmsItemStack.getTag().setString(key, value);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    public int getInt(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag())
            nmsItemStack.setTag(new NBTTagCompound());
        return nmsItemStack.getTag().getInt(key);
    }

    public String getString(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag())
            nmsItemStack.setTag(new NBTTagCompound());
        return nmsItemStack.getTag().getString(key);
    }

    public boolean hasKey(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag())
            return false;
        return nmsItemStack.getTag().hasKey(key);
    }
}
