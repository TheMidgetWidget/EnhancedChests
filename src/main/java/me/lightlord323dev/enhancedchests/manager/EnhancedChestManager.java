package me.lightlord323dev.enhancedchests.manager;

import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.enhancedchests.Main;
import me.lightlord323dev.enhancedchests.api.echest.EnhancedChest;
import me.lightlord323dev.enhancedchests.api.file.GsonUtil;
import me.lightlord323dev.enhancedchests.api.manager.Manager;
import me.lightlord323dev.enhancedchests.item.ECFactory;
import me.lightlord323dev.enhancedchests.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luda on 7/16/2020.
 */
public class EnhancedChestManager implements Manager, Listener {

    private List<EnhancedChest> enhancedChests;

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (ECFactory.isECItem(e.getItemInHand()))
            enhancedChests.add(new EnhancedChest(e.getPlayer().getUniqueId(), ECFactory.getECItemSize(e.getItemInHand()), e.getPlayer().getWorld().getName(), new int[]{e.getBlock().getLocation().getBlockX(), e.getBlock().getLocation().getBlockY(), e.getBlock().getLocation().getBlockZ()}));
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.CHEST) {
            EnhancedChest enhancedChest = getEnhancedChest(e.getBlock());
            if (enhancedChest != null) {
                enhancedChests.remove(enhancedChest.dropInventory());
            }
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            EnhancedChest enhancedChest = getEnhancedChest(e.getClickedBlock());
            if (enhancedChest != null) {
                e.setCancelled(true);
                enhancedChest.openInventory(e.getPlayer(), 1);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null && e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
                if (new NBTUtil(e.getCurrentItem()).hasKey("ecInv"))
                    e.setCancelled(true);
            } else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                NBTUtil nbtUtil = new NBTUtil(e.getCurrentItem());
                if (nbtUtil.hasKey("ecInv")) {
                    player.closeInventory();
                    int nextPage = e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE ? nbtUtil.getInt("ecInv") + 1 : nbtUtil.getInt("ecInv") - 1;
                    getEnhancedChest(nbtUtil.getString("ecLoc")).openInventory(player, nextPage);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer().hasMetadata("ecInvOpen")) {
            String[] data = e.getPlayer().getMetadata("ecInvOpen").get(0).asString().split("!");
            getEnhancedChest(data[0]).saveInventory(e.getInventory(), Integer.parseInt(data[1]));
            e.getPlayer().removeMetadata("ecInvOpen", Main.getInstance());
        }
    }

    @Override
    public void onLoad() {
        enhancedChests = GsonUtil.loadObject(new TypeToken<List<EnhancedChest>>() {
        }, Main.getInstance().getEnhancedChestFile().getFile());
        if (enhancedChests == null)
            enhancedChests = new ArrayList<>();
        else
            enhancedChests.forEach(enhancedChest -> enhancedChest.load());
    }

    @Override
    public void onUnload() {
        enhancedChests.forEach(enhancedChest -> enhancedChest.unload());
        GsonUtil.saveObject(enhancedChests, Main.getInstance().getEnhancedChestFile().getFile());
    }

    /**
     * Checks if given block is an enhanced chest
     *
     * @param block
     * @return EnhancedChest obj if found, null if not found
     */
    public EnhancedChest getEnhancedChest(Block block) {
        return enhancedChests.stream().filter(enhancedChest -> enhancedChest.getLocation()[0] == block.getLocation().getBlockX() && enhancedChest.getLocation()[1] == block.getLocation().getBlockY() && enhancedChest.getLocation()[2] == block.getLocation().getBlockZ()).findAny().orElse(null);
    }

    /**
     * Checks if location contains enhanced chest
     *
     * @param location
     * @return EnhancedChest obj if found, null if not found
     */
    public EnhancedChest getEnhancedChest(int[] location) {
        return enhancedChests.stream().filter(enhancedChest -> enhancedChest.getLocation()[0] == location[0] && enhancedChest.getLocation()[1] == location[1] && enhancedChest.getLocation()[2] == location[2]).findAny().orElse(null);
    }

    /**
     * Checks if location contains enhanced chest
     *
     * @param serializedLocation
     * @return EnhancedChest obj if found, null if not found
     */
    public EnhancedChest getEnhancedChest(String serializedLocation) {
        return enhancedChests.stream().filter(enhancedChest -> enhancedChest.getSerializedLocation().equalsIgnoreCase(serializedLocation)).findAny().orElse(null);
    }
}
