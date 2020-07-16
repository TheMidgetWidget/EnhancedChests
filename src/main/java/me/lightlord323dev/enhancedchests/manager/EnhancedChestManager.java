package me.lightlord323dev.enhancedchests.manager;

import me.lightlord323dev.enhancedchests.api.echest.EnhancedChest;
import me.lightlord323dev.enhancedchests.api.manager.Manager;
import me.lightlord323dev.enhancedchests.item.ECFactory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
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
    public void onOpen(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            EnhancedChest enhancedChest = getEnhancedChest(e.getClickedBlock());
            if (enhancedChest != null) {
                e.setCancelled(true);
                enhancedChest.openInventory(e.getPlayer(), 1);
            }
        }
    }

    @Override
    public void onLoad() {
        enhancedChests = new ArrayList<>();
        // TODO load chests
    }

    @Override
    public void onUnload() {
        // TODO save chests
    }

    public EnhancedChest getEnhancedChest(Block block) {
        return enhancedChests.stream().filter(enhancedChest -> enhancedChest.getLocation()[0] == block.getLocation().getBlockX() && enhancedChest.getLocation()[1] == block.getLocation().getBlockY() && enhancedChest.getLocation()[2] == block.getLocation().getBlockZ()).findAny().orElse(null);
    }
}
