package io.github.nickacpt.buildingassister.commands;

import io.github.nickacpt.buildingassister.gui.MirrorSettingsGui;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MirrorCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            new MirrorSettingsGui(player).show(player);
            return true;
        }
        return false;
    }
}
