package me.lightlord323dev.enhancedchests.item;

import me.lightlord323dev.enhancedchests.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Luda on 7/16/2020.
 */
public class ECFactory {

    public static ItemStack createECItem(int size) {
        return new NBTUtil(new ItemStack(Material.CHEST)).setInt("ecSize", size).getItemStack();
    }

    public static boolean isECItem(ItemStack item) {
        return new NBTUtil(item).hasKey("ecSize");
    }

    public static int getECItemSize(ItemStack item) {
        return new NBTUtil(item).getInt("ecSize");
    }

}
