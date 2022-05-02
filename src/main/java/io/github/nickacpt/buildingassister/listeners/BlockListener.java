package io.github.nickacpt.buildingassister.listeners;

import io.github.nickacpt.buildingassister.logic.MirrorLogic;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        MirrorLogic.performBlockChangeMirror(e.getPlayer(), e.getBlock(), e.getBlock().getBlockData());
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        MirrorLogic.performBlockChangeMirror(e.getPlayer(), e.getBlock(), Bukkit.createBlockData(Material.AIR));
    }

}
