package me.lightlord323dev.enhancedchests.api.echest;

import me.lightlord323dev.enhancedchests.Main;
import me.lightlord323dev.enhancedchests.item.ECFactory;
import me.lightlord323dev.enhancedchests.util.ItemBuilder;
import me.lightlord323dev.enhancedchests.util.ItemSerializer;
import me.lightlord323dev.enhancedchests.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

/**
 * Created by Luda on 7/15/2020.
 */
public class EnhancedChest {

    private UUID owner;
    private int size;
    private String world;
    private int[] location;
    private transient ItemStack[] items;
    private transient long lastUsed;
    private String[] serializedItems;
    private String serializedLocation;
    private int lastPage;

    public EnhancedChest(UUID owner, int size, String world, int[] location) {
        this.owner = owner;
        this.size = size;
        this.world = world;
        this.location = location;
        this.items = new ItemStack[size];
        this.serializedLocation = LocationUtil.serializeLocation(world, location);
        this.lastPage = (int) Math.ceil(size / 45.0);
        this.lastUsed = System.currentTimeMillis();
    }

    public void load() {
        items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            if (serializedItems[i] != null)
                items[i] = ItemSerializer.itemStackFromBase64(serializedItems[i]);
            else
                serializedItems[i] = null;
        }
        serializedItems = null;
        this.lastUsed = System.currentTimeMillis();
    }

    public void unload() {
        serializedItems = new String[size];
        for (int i = 0; i < size; i++) {
            if (items[i] != null) {
                serializedItems[i] = ItemSerializer.itemStackToBase64(items[i]);
            } else
                items[i] = null;
        }
    }

    public void setAvailable(boolean state) {
        Block block = Bukkit.getWorld(world).getBlockAt(location[0], location[1], location[2]);
        if (state) {
            if (block.hasMetadata("ecOpen"))
                block.removeMetadata("ecOpen", Main.getInstance());
        } else
            block.setMetadata("ecOpen", new FixedMetadataValue(Main.getInstance(), '0'));

    }

    public EnhancedChest dropInventory() {
        Location loc = new Location(Bukkit.getWorld(world), location[0], location[1], location[2]);
        for (int i = 0; i < size; i++) {
            if (items[i] != null)
                loc.getWorld().dropItemNaturally(loc, items[i]);
        }
        loc.getWorld().dropItemNaturally(loc, ECFactory.createECItem(size));
        return this;
    }

    public void openInventory(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, 54);
        int translation = (page - 1) * 45;

        /* ITEMSTACK UI STUFF */
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        filler = Main.getInstance().getNbtUtil().setBoolean(filler, "ecInv", true);
        ItemStack nextPage = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(ChatColor.GREEN + "Next Page").build();
        nextPage = Main.getInstance().getNbtUtil().setInt(nextPage, "ecInv", page);
        nextPage = Main.getInstance().getNbtUtil().setString(nextPage, "ecLoc", serializedLocation);
        ItemStack prevPage = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(ChatColor.RED + "Previous Page").build();
        prevPage = Main.getInstance().getNbtUtil().setInt(prevPage, "ecInv", page);
        prevPage = Main.getInstance().getNbtUtil().setString(prevPage, "ecLoc", serializedLocation);

        if (size < 45) {
            for (int i = 0; i < size; i++) {
                if (items[i] != null)
                    inventory.setItem(i, items[i]);
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

        // last row
        for (int i = 45; i < 54; i++) {
            if (page != lastPage && i > 49) {
                inventory.setItem(i, nextPage);
            } else if (page != 1 && i < 49) {
                inventory.setItem(i, prevPage);
            } else
                inventory.setItem(i, filler);
        }

        // fill empty space in last page
        if (page == lastPage) {
            for (int i = (size - translation); i < 45; i++) {
                inventory.setItem(i, filler);
            }
        }

        player.openInventory(inventory);
        player.setMetadata("ecInvOpen", new FixedMetadataValue(Main.getInstance(), serializedLocation + "!" + page));
        this.lastUsed = System.currentTimeMillis();
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

    public void sort(Player player) {
        player.closeInventory();
        // initial sort
        Arrays.sort(items, Comparator.nullsLast(Comparator.comparing(ItemStack::getType)));
        // grouping stackable items
        int toSort = 0;
        for (int i = 1; i < size; i++) {
            if (items[toSort] != null && items[i] != null && items[toSort].getAmount() < items[toSort].getMaxStackSize() && items[toSort].getType() == items[i].getType()) {
                int amt = items[toSort].getMaxStackSize() - items[toSort].getAmount();
                if (items[i].getAmount() > amt) {
                    items[toSort].setAmount(64);
                    items[i].setAmount(items[i].getAmount() - amt);
                    toSort = i;
                } else {
                    items[toSort].setAmount(items[toSort].getAmount() + items[i].getAmount());
                    items[i] = null;
                }
            } else
                toSort = i;
        }
        // final sort
        Arrays.sort(items, Comparator.nullsLast(Comparator.comparing(ItemStack::getType)));
        this.lastUsed = System.currentTimeMillis();
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

    public void setSerializedItems(String[] serializedItems) {
        this.serializedItems = serializedItems;
    }

    public long getLastUsed() {
        return lastUsed;
    }
}
