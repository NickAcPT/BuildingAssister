package io.github.nickacpt.buildingassister.model;

import java.util.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerMirrorSettingsStorage {
    private static final Map<UUID, PlayerMirrorSettings> playerMirrorSettings = new HashMap<>();

    @Nullable
    public static PlayerMirrorSettings getPlayerMirrorSettings(Player player) {
        return playerMirrorSettings.computeIfAbsent(player.getUniqueId(),
                uuid -> new PlayerMirrorSettings(new Location(player.getWorld(), 8.5, 4, 8.5), true, false, true, true,
                        false, true));
    }
}
