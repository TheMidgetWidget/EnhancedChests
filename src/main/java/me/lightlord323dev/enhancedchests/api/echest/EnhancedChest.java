package me.lightlord323dev.enhancedchests.api.echest;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Luda on 7/15/2020.
 */
public class EnhancedChest {

    private UUID owner;
    private int size;
    private String world;
    private int[] location;
    private ItemStack[] items;

    public EnhancedChest(UUID owner, int size, String world, int[] location) {
        this.owner = owner;
        this.size = size;
        this.world = world;
        this.location = location;
        this.items = new ItemStack[size];
    }

    public void openInventory(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, 54);

        if (size < 45) {
            for (int i = 0; i < size; i++) {
                if (items[i] != null)
                    inventory.setItem(i, items[i]);
            }
        } else {
            for (int i = 0; i < 45; i++) {
                if (items[i] != null)
                    inventory.setItem(i, items[i]);
            }
        }

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack nextPage = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemStack prevPage = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        int lastPage = (int) Math.ceil(size / 45.0);

        if (page != lastPage)
            inventory.setItem(53, nextPage);

        if (page != 1)
            inventory.setItem(45, prevPage);

        if (page == lastPage) {
            for (int i = size; i < 45; i++) {
                inventory.setItem(i, filler);
            }
        }

        player.openInventory(inventory);
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
