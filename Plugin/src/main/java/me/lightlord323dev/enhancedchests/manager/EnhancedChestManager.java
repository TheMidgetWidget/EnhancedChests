package me.lightlord323dev.enhancedchests.manager;

import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.enhancedchests.Main;
import me.lightlord323dev.enhancedchests.api.echest.EnhancedChest;
import me.lightlord323dev.enhancedchests.api.file.AbstractFile;
import me.lightlord323dev.enhancedchests.api.file.GsonUtil;
import me.lightlord323dev.enhancedchests.api.manager.Manager;
import me.lightlord323dev.enhancedchests.item.ECFactory;
import me.lightlord323dev.enhancedchests.util.LocationUtil;
import me.lightlord323dev.enhancedchests.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luda on 7/16/2020.
 */
public class EnhancedChestManager implements Manager, Listener {

    private List<EnhancedChest> enhancedChests;
    private ScheduledExecutorService executorService;

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (ECFactory.isECItem(e.getItemInHand())) {
            EnhancedChest enhancedChest = new EnhancedChest(e.getPlayer().getUniqueId(), ECFactory.getECItemSize(e.getItemInHand()), e.getPlayer().getWorld().getName(), new int[]{e.getBlock().getLocation().getBlockX(), e.getBlock().getLocation().getBlockY(), e.getBlock().getLocation().getBlockZ()});
            saveEnhancedChestData(enhancedChest);
            enhancedChests.add(enhancedChest);
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.CHEST) {
            loadEnhancedChestData(e.getBlock(), () -> {
                EnhancedChest enhancedChest = getEnhancedChest(e.getBlock());
                if (enhancedChest != null) {

                    // setDropItems only works for certain versions
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);

                    enhancedChest.dropInventory();
                    executorService.schedule(() -> {
                        File file = new AbstractFile(Main.getInstance(), enhancedChest.getSerializedLocation() + ".json", false, false).getFile();
                        if (file != null) {
                            file.delete();
                        }
                        enhancedChests.remove(enhancedChest);
                    }, 0, TimeUnit.MILLISECONDS);
                }
            });
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        e.blockList().forEach(block -> {
            if (block.getType() == Material.CHEST) {
                loadEnhancedChestData(block, () -> {
                    EnhancedChest enhancedChest = getEnhancedChest(block);
                    if (enhancedChest != null) {
                        enhancedChest.dropInventory();
                        executorService.schedule(() -> {
                            File file = new AbstractFile(Main.getInstance(), enhancedChest.getSerializedLocation() + ".json", false, false).getFile();
                            if (file != null) {
                                file.delete();
                            }
                            enhancedChests.remove(enhancedChest);
                        }, 0, TimeUnit.MILLISECONDS);
                    }

                });
            }
        });
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            loadEnhancedChestData(e.getClickedBlock(), () -> {
                EnhancedChest enhancedChest = getEnhancedChest(e.getClickedBlock());
                if (enhancedChest != null) {
                    if (isEnhancedChestAvailable(e.getClickedBlock())) {
                        e.setCancelled(true);
                        enhancedChest.openInventory(e.getPlayer(), 1);
                        enhancedChest.setAvailable(false);
                    } else {
                        MessageUtil.error(e.getPlayer(), "Another player is using this enhanced chest.");
                    }
                }
            });
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null && e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
                if (Main.getInstance().getNbtUtil().hasKey(e.getCurrentItem(), "ecInv"))
                    e.setCancelled(true);
            } else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                if (Main.getInstance().getNbtUtil().hasKey(e.getCurrentItem(), "ecInv")) {
                    player.closeInventory();
                    int currentPage = Main.getInstance().getNbtUtil().getInt(e.getCurrentItem(), "ecInv"), nextPage = e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE ? currentPage + 1 : currentPage - 1;
                    getEnhancedChest(Main.getInstance().getNbtUtil().getString(e.getCurrentItem(), "ecLoc")).openInventory(player, nextPage);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer().hasMetadata("ecInvOpen")) {
            String[] data = e.getPlayer().getMetadata("ecInvOpen").get(0).asString().split("!");
            EnhancedChest enhancedChest = getEnhancedChest(data[0]);
            enhancedChest.saveInventory(e.getInventory(), Integer.parseInt(data[1]));
            enhancedChest.setAvailable(true);
            e.getPlayer().removeMetadata("ecInvOpen", Main.getInstance());
        }
    }

    @Override
    public void onLoad() {
        this.enhancedChests = new ArrayList<>();
        executorService = Executors.newScheduledThreadPool(4);
        executorService.scheduleAtFixedRate(() -> {
            MessageUtil.log("Saving enhanced chest data...");
            onUnload();
            enhancedChests.forEach(enhancedChest -> enhancedChest.setSerializedItems(null));
            MessageUtil.log("Enhanced chest data saved!");
            enhancedChests.stream().filter(enhancedChest -> System.currentTimeMillis() - enhancedChest.getLastUsed() >= 300000).forEach(enhancedChest -> unloadAndSaveEnhancedChestData(enhancedChest));
        }, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public void onUnload() {
        enhancedChests.forEach(enhancedChest -> {
            enhancedChest.unload();
            GsonUtil.saveObject(enhancedChests, new AbstractFile(Main.getInstance(), enhancedChest.getSerializedLocation() + ".json", false, true).getFile());
        });
    }

    /**
     * Checks if given block is an enhanced chest
     *
     * @param block
     * @return EnhancedChest obj if found, null if not found
     */

    public EnhancedChest getEnhancedChest(Block block) {
        if (block.getType() != Material.CHEST)
            return null;
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

    /**
     * checks if enhanced chest is available for player to open
     *
     * @param block
     * @return true if available, false if not available
     */
    public boolean isEnhancedChestAvailable(Block block) {
        return !block.hasMetadata("ecOpen");
    }


    /**
     * loads chest data into memory
     *
     * @param block the chest to be loaded
     */
    private void loadEnhancedChestData(Block block) {
        if (getEnhancedChest(block) == null) {
            this.executorService.schedule(() -> {
                File file = new AbstractFile(Main.getInstance(), LocationUtil.serializeLocation(block.getLocation()) + ".json", false, false).getFile();
                if (file == null)
                    return;
                EnhancedChest[] arr = GsonUtil.loadObject(new TypeToken<EnhancedChest[]>() {
                }, file);
                if (arr == null)
                    return;
                EnhancedChest enhancedChest = arr[0];
                enhancedChest.load();
                enhancedChests.add(enhancedChest);
            }, 0, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * loads chest data into memory and runs runnable after loading
     *
     * @param block the chest to be loaded
     */
    private void loadEnhancedChestData(Block block, Runnable runnable) {

        this.executorService.schedule(() -> {
            if (getEnhancedChest(block) == null) {
                File file = new AbstractFile(Main.getInstance(), LocationUtil.serializeLocation(block.getLocation()) + ".json", false, false).getFile();
                if (file != null) {

                    EnhancedChest[] arr = GsonUtil.loadObject(new TypeToken<EnhancedChest[]>() {
                    }, file);
                    if (arr != null) {
                        EnhancedChest enhancedChest = arr[0];
                        enhancedChest.load();
                        enhancedChests.add(enhancedChest);
                    }
                }
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), runnable);
        }, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * loads chest data into memory and opens the chest for player if found
     *
     * @param block the chest to be loaded
     */
    private void loadEnhancedChestData(Block block, Player player) {
        if (getEnhancedChest(block) == null) {
            this.executorService.schedule(() -> {
                File file = new AbstractFile(Main.getInstance(), LocationUtil.serializeLocation(block.getLocation()) + ".json", false, false).getFile();
                if (file == null)
                    return;
                EnhancedChest[] arr = GsonUtil.loadObject(new TypeToken<EnhancedChest[]>() {
                }, file);
                if (arr == null)
                    return;
                EnhancedChest enhancedChest = arr[0];
                enhancedChest.load();
                enhancedChests.add(enhancedChest);
                enhancedChest.setAvailable(false);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> enhancedChest.openInventory(player, 1));
            }, 0, TimeUnit.MILLISECONDS);
        }
    }

    private void unloadAndSaveEnhancedChestData(EnhancedChest enhancedChest) {
        enhancedChest.unload();
        saveEnhancedChestData(enhancedChest);
        this.enhancedChests.remove(enhancedChest);
    }

    private void saveEnhancedChestData(EnhancedChest enhancedChest) {
        GsonUtil.saveObject(enhancedChest, new AbstractFile(Main.getInstance(), enhancedChest.getSerializedLocation() + ".json", false, true).getFile());
    }
}
