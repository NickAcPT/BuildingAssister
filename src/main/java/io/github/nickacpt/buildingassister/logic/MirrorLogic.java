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

        EnumSet<MirrorAxis> axisToMirror = settings.axisToMirror();
        Set<Vector> finalLocations = new HashSet<>();

        BlockVector originalLocation = new BlockVector(changedBlock.getX(), changedBlock.getY(), changedBlock.getZ());
        finalLocations.add(originalLocation);

        int expectedLocations = Math.max(axisToMirror.size() * 2, 1);
        int recursionTries = 0;

        while (finalLocations.size() >= expectedLocations && recursionTries <= 50) {
            for (Vector blockLocation : finalLocations) {
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
                        case XZ, ZX -> {
                            throw new RuntimeException("Not implemented");
                        }
                    }
                    finalLocations.add(newVector);
                }
            }
            recursionTries++;
        }


        finalLocations.forEach(v -> {
            Location location = v.toLocation(changedBlock.getWorld()).toCenterLocation();
            changedBlock.getWorld().setBlockData(location, blockData);
        });

    }


}
