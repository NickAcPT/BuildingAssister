package io.github.nickacpt.buildingassister.logic;

import io.github.nickacpt.buildingassister.model.MirrorAxis;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import java.util.*;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

record VectorWithBlockRotation(Vector vector, EnumSet<Mirror> mirrors, EnumSet<Rotation> rotations) {
    public VectorWithBlockRotation(Vector vector) {
        this(vector, EnumSet.noneOf(Mirror.class), EnumSet.noneOf(Rotation.class));
    }
}

public class MirrorLogic {
    public static void performBlockChangeMirror(Player player, @NotNull Block changedBlock, @NotNull BlockData blockData) {
        var settings = PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(player);
        if (!settings.enabled()) return;
        Location centerLocation = settings.centerLocation();
        if (centerLocation == null) return;

        EnumSet<MirrorAxis> axisToMirror = settings.axisToMirror();
        Set<VectorWithBlockRotation> finalLocations = new HashSet<>();

        Vector centerLocationVect = centerLocation.toCenterLocation().toVector();
        BlockVector originalLocation = changedBlock.getLocation().toCenterLocation().subtract(centerLocationVect)
                .toVector()
                .toBlockVector();
        finalLocations.add(new VectorWithBlockRotation(originalLocation));

        int maxTries = axisToMirror.size();
        BlockFace placedFace = BlockFace.SELF;
        if (blockData instanceof Directional) placedFace = ((Directional) blockData).getFacing();

        for (int i = 0; i < maxTries; i++) {
            for (VectorWithBlockRotation vectorWithBlockRotation : Set.copyOf(finalLocations)) {
                for (MirrorAxis mirrorAxis : axisToMirror) {
                    Vector blockLocation = vectorWithBlockRotation.vector();
                    Vector newVector = blockLocation.clone();
                    EnumSet<Mirror> mirrors = EnumSet.noneOf(Mirror.class);
                    EnumSet<Rotation> rotations = EnumSet.noneOf(Rotation.class);

                    switch (mirrorAxis) {
                        case X -> {
                            newVector.setX(blockLocation.getX() * -1);
                            mirrors.add(Mirror.FRONT_BACK);
                        }
                        case Y -> {
                            newVector.setY(blockLocation.getY() * -1);
                        }
                        case Z -> {
                            newVector.setZ(blockLocation.getZ() * -1);
                            mirrors.add(Mirror.LEFT_RIGHT);
                        }
                        case ZX, XZ -> {
                            var oldX = newVector.getX();
                            newVector.setX(newVector.getZ());
                            newVector.setZ(oldX);

                            if (mirrorAxis == MirrorAxis.XZ) {
                                newVector.setX(newVector.getX() * -1);
                                newVector.setZ(newVector.getZ() * -1);
                            }

                            if (placedFace.getModZ() == 0) {
                                rotations.add(Rotation.CLOCKWISE_90);
                                mirrors.add(Mirror.LEFT_RIGHT);
                            } else {
                                rotations.add(Rotation.COUNTERCLOCKWISE_90);
                                mirrors.add(Mirror.FRONT_BACK);
                            }


                        }
                    }
                    var existing = finalLocations.stream().filter(it -> it.vector().equals(newVector)).findFirst()
                            .orElse(null);
                    if (existing != null) {
                        existing.mirrors().addAll(mirrors);
                        existing.rotations().addAll(rotations);
                    } else {
                        finalLocations.add(new VectorWithBlockRotation(newVector, mirrors, rotations));
                    }
                }
            }
        }

        BlockFace finalPlacedFace = placedFace;
        finalLocations.forEach(loc -> {
            if (loc.vector().equals(originalLocation)) return;

            var v = loc.vector();
            Location location = v.add(centerLocationVect).toLocation(changedBlock.getWorld());
            changedBlock.getWorld().setBlockData(location, blockData);

            CraftBlockState blockState = (CraftBlockState) changedBlock.getWorld().getBlockState(location);
            player.sendMessage(Component.text(finalPlacedFace.toString()));
            //player.sendMessage(Component.text(blockData.toString()));

            for (Rotation rotation : loc.rotations()) blockState.setData(blockState.getHandle().rotate(rotation));
            for (Mirror mirror : loc.mirrors()) blockState.setData(blockState.getHandle().mirror(mirror));

            String mirrorsDebug = loc.mirrors().stream().map(Enum::name).collect(
                    Collectors.joining(", "));
            TextComponent rotationsDebug = Component.text(loc.rotations().stream().map(Enum::name).collect(
                    Collectors.joining(", ")));
            TextComponent debug = Component.text(mirrorsDebug).append(Component.text(", ").append(rotationsDebug));
            //player.sendMessage(debug.clickEvent(ClickEvent.runCommand(
             //               "/tp " + location.getX() + " " + location.getY() + " " + location.getZ() + " ")));

            blockState.update(true, true);
        });
    }
}
