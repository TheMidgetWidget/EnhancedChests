package me.lightlord323dev.enhancedchests.util;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Luda on 7/17/2020.
 */
public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amt) {
        this.itemStack = new ItemStack(material, amt);
    }

    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(Lists.newArrayList(lore));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}
