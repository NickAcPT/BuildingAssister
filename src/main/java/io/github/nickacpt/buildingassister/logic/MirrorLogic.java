package io.github.nickacpt.buildingassister.logic;

import com.mojang.math.OctahedralGroup;
import io.github.nickacpt.buildingassister.model.MirrorAxis;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import java.util.*;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

record VectorWithBlockRotation(@Nullable MirrorAxis axis, Vector vector, EnumSet<Mirror> mirrors,
                               EnumSet<Rotation> rotations,
                               EnumSet<OctahedralGroup> octahedralGroups, @Nullable VectorWithBlockRotation source) {
    public VectorWithBlockRotation(@Nullable MirrorAxis axis, Vector vector) {
        this(axis, vector, EnumSet.noneOf(Mirror.class), EnumSet.noneOf(Rotation.class),
                EnumSet.noneOf(OctahedralGroup.class), null);
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
        finalLocations.add(new VectorWithBlockRotation(null, originalLocation));

        int maxTries = axisToMirror.size() + 1;
        BlockFace placedFace = BlockFace.SELF;
        if (blockData instanceof Directional) placedFace = ((Directional) blockData).getFacing();

        for (int i = 0; i < maxTries; i++) {
            for (VectorWithBlockRotation vectorWithBlockRotation : Set.copyOf(finalLocations)) {
                List<MirrorAxis> mirrorAxes = new ArrayList<>(axisToMirror);
                mirrorAxes.sort(Comparator.comparingInt(MirrorAxis::ordinal));

                for (MirrorAxis mirrorAxis : mirrorAxes) {
                    Vector blockLocation = vectorWithBlockRotation.vector();
                    Vector newVector = blockLocation.clone();
                    EnumSet<OctahedralGroup> octahedralGroups = EnumSet.noneOf(OctahedralGroup.class);
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

                            BlockFace face = blockData instanceof Directional ? ((Directional) blockData).getFacing() : BlockFace.SELF;

                            int mod = (mirrorAxis.getLeadingAxis() == MirrorAxis.X) ? face.getModX() : face.getModZ();

                            if (mirrorAxis.getLeadingAxis() == MirrorAxis.X) {
                                mirrors.add(mod == 0 ? Mirror.LEFT_RIGHT : Mirror.FRONT_BACK);
                                octahedralGroups.add(OctahedralGroup.SWAP_NEG_XZ);
                            } else {
                                octahedralGroups.add(OctahedralGroup.SWAP_XZ);
                            }
                        }
                    }
                    var existing = finalLocations.stream().filter(it -> it.vector().equals(newVector)).findFirst()
                            .orElse(null);
                    if (existing != null) {
                        existing.mirrors().addAll(mirrors);
                        existing.rotations().addAll(rotations);
                        existing.octahedralGroups().addAll(octahedralGroups);
                    } else {
                        finalLocations.add(
                                new VectorWithBlockRotation(mirrorAxis, newVector, mirrors, rotations, octahedralGroups,
                                        vectorWithBlockRotation));
                    }
                }
            }
        }

        finalLocations.forEach(loc -> {
            if (loc.vector().equals(originalLocation)) return;

            var v = loc.vector();
            Location location = v.add(centerLocationVect).toLocation(changedBlock.getWorld());
            changedBlock.getWorld().setBlockData(location, blockData);

            org.bukkit.block.BlockState originalBlockState = changedBlock.getWorld().getBlockState(location);
            CraftBlockState craftBlockState = (CraftBlockState) originalBlockState;

            BlockState finalNmsState = craftBlockState.getHandle();

            for (Mirror mirror : loc.mirrors()) finalNmsState = finalNmsState.mirror(mirror);
            for (Rotation rotation : loc.rotations()) finalNmsState = finalNmsState.rotate(rotation);

            craftBlockState.setData(finalNmsState);

            BlockData finalBlockData = craftBlockState.getBlockData();
            if (finalBlockData instanceof Directional directional) {
                Direction finalNmsDirection = CraftBlock.blockFaceToNotch(directional.getFacing());

                for (OctahedralGroup octahedralGroup : loc.octahedralGroups())
                    finalNmsDirection = octahedralGroup.rotate(finalNmsDirection);

                directional.setFacing(CraftBlock.notchToBlockFace(finalNmsDirection));

                craftBlockState.setBlockData(finalBlockData);
            }

            String mirrorsDebug = loc.mirrors().stream().map(Enum::name).collect(
                    Collectors.joining(", "));
            TextComponent rotationsDebug = Component.text(loc.rotations().stream().map(Enum::name).collect(
                    Collectors.joining(", ")));
            TextComponent octahedralGroupsDebug = Component.text(
                    loc.octahedralGroups().stream().map(Enum::name).collect(
                            Collectors.joining(", ")));


            Component debug = MiniMessage.miniMessage().deserialize(
                    "<blue>Debug></blue> <gold>Placed <mirror_block> mirrored from <src_block> <red>(<axis><red>)",

                    Placeholder.parsed("mirror_block",

                            "<click:run_command:" +
                                    "'/tp " + location.getX() + " " + location.getY() + " " + location.getZ() + "'" +
                                    "><yellow>[Block]</yellow></click>"
                    ),

                    Placeholder.parsed("src_block",
                                    (loc.source() != null ? ("<click:run_command:'/tp " + loc.source().vector().getX() + " " + loc.source()
                                            .vector().getY() + " " + loc.source().vector()
                                            .getZ() + "'") : "<hover:'<red>Unknown source'") +
                                    "><yellow>[Mirror Source]</yellow>"
                    ),

                    Placeholder.parsed("axis",
                            "<yellow>" + (loc.axis() != null ? loc.axis().name() : "Original Block") + ""
                    )
            );

            //TextComponent debug =
            // Placed [Block] sourced [Mirror]

            //player.sendMessage(debug);

            craftBlockState.update(true, true);
        });
    }

}
