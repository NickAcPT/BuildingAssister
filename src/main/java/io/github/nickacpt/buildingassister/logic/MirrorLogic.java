package io.github.nickacpt.buildingassister.logic;

import io.github.nickacpt.buildingassister.model.MirrorAxis;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class MirrorLogic {
    public static void performBlockChangeMirror(Player player, @NotNull Block changedBlock, @NotNull BlockData blockData) {
        var settings = PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(player);
        if (!settings.enabled()) return;
        Location centerLocation = settings.centerLocation();
        if (centerLocation == null) return;

        EnumSet<MirrorAxis> axisToMirror = settings.axisToMirror();
        Set<Vector> finalLocations = new HashSet<>();

        Vector centerLocationVect = centerLocation.toCenterLocation().toVector();
        BlockVector originalLocation = changedBlock.getLocation().toCenterLocation().subtract(centerLocationVect).toVector()
                .toBlockVector();
        finalLocations.add(originalLocation);

        int maxTries = axisToMirror.size();

        for (int i = 0; i < maxTries; i++) {
            for (Vector blockLocation : Set.copyOf(finalLocations)) {
                for (MirrorAxis mirrorAxis : axisToMirror) {
                    Vector newVector = blockLocation.clone();

                    switch (mirrorAxis) {
                        case X -> {
                            newVector.setX(blockLocation.getX() * -1);
                        }
                        case Y -> {
                            newVector.setY(blockLocation.getY() * -1);
                        }
                        case Z -> {
                            newVector.setZ(blockLocation.getZ() * -1);
                        }
                        case ZX, XZ -> {
                            var oldX = newVector.getX();
                            newVector.setX(newVector.getZ());
                            newVector.setZ(oldX);

                            if (mirrorAxis == MirrorAxis.XZ) {
                                newVector.setX(newVector.getX() * -1);
                                newVector.setZ(newVector.getZ() * -1);
                            }
                        }
                    }
                    finalLocations.add(newVector);
                }
            }
        }

        finalLocations.forEach(v -> {
            changedBlock.getWorld().setBlockData(v.add(centerLocationVect).toLocation(changedBlock.getWorld()), blockData);
        });
    }
}
