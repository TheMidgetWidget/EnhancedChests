package me.lightlord323dev.enhancedchests.api.user;

import me.lightlord323dev.enhancedchests.api.echest.EnchancedChest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luda on 7/15/2020.
 */
public class ECUser {

    private UUID playerUUID;
    private List<EnchancedChest> enchancedChests;

    public ECUser(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.enchancedChests = new ArrayList<>();
    }

    public ECUser(UUID playerUUID, List<EnchancedChest> enchancedChests) {
        this.playerUUID = playerUUID;
        this.enchancedChests = enchancedChests;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public List<EnchancedChest> getEnchancedChests() {
        return enchancedChests;
    }
}
