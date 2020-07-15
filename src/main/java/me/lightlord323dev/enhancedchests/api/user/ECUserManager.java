package me.lightlord323dev.enhancedchests.api.user;

import me.lightlord323dev.enhancedchests.api.manager.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 7/15/2020.
 */
public class ECUserManager implements Manager {

    private List<ECUser> users;

    @Override
    public void onLoad() {
        users = new ArrayList<>();
    }

    @Override
    public void onUnload() {
        // TODO SAVE CACHED USERS
    }

    /**
     *
     * @param uuid player uuid
     * @return ECUser object if found, null if not found
     */
    public ECUser getUser(UUID uuid) {
        return users.stream().filter(ecUser -> ecUser.getPlayerUUID().equals(uuid)).findAny().orElse(null);
    }

}
