package me.lightlord323dev.enhancedchests.api.user;

import me.lightlord323dev.enhancedchests.api.manager.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 7/15/2020.
 */
public class ECUserManager implements Manager, Listener {

    private List<ECUser> users;

    @Override
    public void onLoad() {
        users = new ArrayList<>();
    }

    @Override
    public void onUnload() {
        // TODO SAVE CACHED USERS
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // TODO check for saved data, will create user objects for now
        registerUser(new ECUser(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        // TODO save player data
        unregisterUser(e.getPlayer());
    }

    /**
     * @param uuid player uuid
     * @return ECUser object if found, null if not found
     */
    public ECUser getUser(UUID uuid) {
        return users.stream().filter(ecUser -> ecUser.getPlayerUUID().equals(uuid)).findAny().orElse(null);
    }

    /**
     * caches user in manager
     * @param user
     */
    private void registerUser(ECUser user) {
        users.add(user);
    }

    /**
     * de-caches user info
     * @param user
     */
    private void unregisterUser(ECUser user) {
        if (users.contains(user))
            users.remove(user);
    }

    /**
     * de-caches user info
     * @param player
     */
    private void unregisterUser(Player player) {
        ECUser user = getUser(player.getUniqueId());
        if (user != null)
            users.remove(user);
    }

    /**
     * de-caches user info
     * @param uuid
     */
    private void unregisterUser(UUID uuid) {
        ECUser user = getUser(uuid);
        if (user != null)
            users.remove(user);
    }

}
