package me.lightlord323dev.enhancedchests.api.echest;

import me.lightlord323dev.enhancedchests.Main;
import me.lightlord323dev.enhancedchests.util.ItemBuilder;
import me.lightlord323dev.enhancedchests.util.LocationUtil;
import me.lightlord323dev.enhancedchests.util.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

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
    private String serializedLocation;

    public EnhancedChest(UUID owner, int size, String world, int[] location) {
        this.owner = owner;
        this.size = size;
        this.world = world;
        this.location = location;
        this.items = new ItemStack[size];
        this.serializedLocation = LocationUtil.serializeLocation(world, location);
    }

    public void openInventory(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, 54);
        int translation = (page - 1) * 45;

        /* ITEMSTACK UI STUFF */
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        filler = new NBTUtil(filler).setBoolean("ecInv", true).getItemStack();
        ItemStack nextPage = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(ChatColor.GREEN + "Next Page").build();
        nextPage = new NBTUtil(nextPage).setInt("ecInv", page).setString("ecLoc", serializedLocation).getItemStack();
        ItemStack prevPage = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(ChatColor.RED + "Previous Page").build();
        prevPage = new NBTUtil(prevPage).setInt("ecInv", page).setString("ecLoc", serializedLocation).getItemStack();

        if (size < 45) {
            for (int i = 0; i < size; i++) {
                if (items[i] != null)
                    inventory.setItem(i, items[i]);
            }
            for (int i = size; i < 45; i++) {
                inventory.setItem(i, filler);
            }
        } else {
            for (int i = 0; i < 45; i++) {
                int actualIndex = i + translation;
                if (actualIndex >= size)
                    break;
                if (items[actualIndex] != null)
                    inventory.setItem(i, items[actualIndex]);
            }
        }

        int lastPage = (int) Math.ceil(size / 45.0);

        // last row
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, filler);
        }

        // fill empty space in last page
        if (page == lastPage && size > 45) {
            for (int i = (size - translation); i < 45; i++) {
                inventory.setItem(i, filler);
            }
        }

        if (page != lastPage)
            inventory.setItem(53, nextPage);

        if (page != 1)
            inventory.setItem(45, prevPage);

        player.openInventory(inventory);
        player.setMetadata("ecInvOpen", new FixedMetadataValue(Main.getInstance(), serializedLocation + "!" + page));
    }

    public void saveInventory(Inventory inventory, int page) {
        int startIndex = 45 * (page - 1);
        int max = size < 45 ? size : 45;
        for (int i = 0; i < max; i++) {
            int actualIndex = startIndex + i;
            if (actualIndex >= size)
                break;
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
                items[actualIndex] = null;
            else {
                items[actualIndex] = inventory.getItem(i);
            }
        }
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

    public String getSerializedLocation() {
        return serializedLocation;
    }
}
