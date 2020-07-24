package me.lightlord323dev.enhancedchests.item;

import me.lightlord323dev.enhancedchests.Main;
import me.lightlord323dev.enhancedchests.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Luda on 7/16/2020.
 */
public class ECFactory {

    public static ItemStack createECItem(int size) {
        ItemStack item = new ItemStack(Material.CHEST);
        item = Main.getInstance().getNbtUtil().setInt(item, "ecSize", size);
        return new ItemBuilder(item).setDisplayName(ChatColor.BLUE + "Enhanced Chest").setLore(ChatColor.BLUE + "Enhanced chest size: " + ChatColor.GOLD + size).build();
    }

    public static boolean isECItem(ItemStack item) {
        return Main.getInstance().getNbtUtil().hasKey(item, "ecSize");
    }

    public static int getECItemSize(ItemStack item) {
        return Main.getInstance().getNbtUtil().getInt(item, "ecSize");
    }

}
