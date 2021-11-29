package io.github.nickacpt.buildingassister.listeners;

import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockListener implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        performBlockChangeMirror(e.getPlayer(), e.getBlock(), e.getBlock().getBlockData());
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        performBlockChangeMirror(e.getPlayer(), e.getBlock(), Bukkit.createBlockData(Material.AIR));
    }

    private void performBlockChangeMirror(Player player, @NotNull Block changedBlock, @NotNull BlockData blockData) {
        var settings = PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(player);
        if (!settings.enabled()) return;
    }
}
