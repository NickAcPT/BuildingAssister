package io.github.nickacpt.buildingassister.model;

import java.util.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerMirrorSettingsStorage {
    private static final Map<UUID, PlayerMirrorSettings> playerMirrorSettings = new HashMap<>();
    private static final Map<UUID, PlayerMirrorSettingsV2> playerMirrorSettingsV2 = new HashMap<>();

    public static @NotNull PlayerMirrorSettings getPlayerMirrorSettings(Player player) {
        return playerMirrorSettings.computeIfAbsent(player.getUniqueId(),
                uuid -> new PlayerMirrorSettings(new Location(player.getWorld(), 8.5, 4, 8.5), true, false, true, true,
                        false, true));
    }

    public static @NotNull PlayerMirrorSettingsV2 getPlayerMirrorSettingsV2(Player player) {
        return playerMirrorSettingsV2.computeIfAbsent(player.getUniqueId(),
                uuid -> new PlayerMirrorSettingsV2());
    }
}
