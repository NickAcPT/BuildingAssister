package io.github.nickacpt.buildingassister.listeners;

import io.github.nickacpt.buildingassister.MirrorUtils;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettings;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
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
        PlayerMirrorSettings settings = PlayerMirrorSettingsStorage.getPlayerMirrorSettings(player);
        if (settings == null) return;

        Location blockPos = changedBlock.getLocation().toCenterLocation();
        List<RotatedLocation> toPlaceLocations = new ArrayList<>();
        toPlaceLocations.add(new RotatedLocation(blockPos, RotationAxis.SELF));

        if (settings.rotateXAxis()) MirrorUtils.rotateX(settings, blockPos.toCenterLocation(), toPlaceLocations);
        if (settings.rotateYAxis()) MirrorUtils.rotateY(settings, blockPos.toCenterLocation(), toPlaceLocations);
        if (settings.rotateZAxis()) MirrorUtils.rotateZ(settings, blockPos.toCenterLocation(), toPlaceLocations, false);

        for (RotatedLocation newLocation : toPlaceLocations) {
            if (newLocation.location().toCenterLocation().equals(blockPos)) continue;

            BlockData finalBlockData = blockData.clone();
            if (finalBlockData instanceof Directional directional) {
                BlockFace mirrored = MirrorUtils.mirrorBlockFace(settings, newLocation.face(), directional.getFacing());
                if (directional.getFaces().contains(mirrored)) {
                    directional.setFacing(mirrored);
                }
            }
            if (finalBlockData instanceof Stairs) {
                Stairs.Shape mirroredShape = MirrorUtils.mirrorShape(settings, newLocation.face(),
                        ((Stairs) finalBlockData).getShape());
                ((Stairs) finalBlockData).setShape(mirroredShape);
            }

            newLocation.location().getBlock().setBlockData(finalBlockData, true);
        }
    }

    @NotNull
    private BlockFace getBlockDataDirection(@NotNull Block changedBlock, @NotNull BlockData blockData) {
        if (blockData.getMaterial().isAir()) {
            blockData = changedBlock.getBlockData();
        }
        return blockData instanceof Directional ? ((Directional) blockData).getFacing() : BlockFace.SELF;
    }
}
