package io.github.nickacpt.buildingassister.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerMirrorSettingsStorage {
    @Nullable
    public static PlayerMirrorSettings getPlayerMirrorSettings(Player player) {
        return new PlayerMirrorSettings(new Location(player.getWorld(), 8.5, 4, 8.5), true, false, true, true, false, true);
    }
}
