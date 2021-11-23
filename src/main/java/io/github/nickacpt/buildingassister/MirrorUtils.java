package io.github.nickacpt.buildingassister;

import io.github.nickacpt.buildingassister.listeners.RotatedLocation;
import io.github.nickacpt.buildingassister.listeners.RotationAxis;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettings;
import java.util.List;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.Nullable;

public class MirrorUtils {

    private static final BlockFace[] blockFacesCache = BlockFace.values();

    public static RotatedLocation rotateX(PlayerMirrorSettings settings, Location blockPos, List<RotatedLocation> toPlaceLocations) {
        var x = MirrorUtils.mirrorLocationValue(settings,
                blockPos,
                Location::getX,
                PlayerMirrorSettings::mirrorXAxis);

        RotatedLocation added = new RotatedLocation(
                (new Location(blockPos.getWorld(), x, blockPos.getBlockY(), blockPos.getZ())),
                RotationAxis.X);
        toPlaceLocations.add(added);

        if (settings.mirrorZAxis()) rotateZ(settings, added.location(), toPlaceLocations, true);
        return added;
    }

    public static RotatedLocation rotateY(PlayerMirrorSettings settings, Location blockPos, List<RotatedLocation> toPlaceLocations) {
        var y = MirrorUtils.mirrorLocationValue(settings,
                blockPos,
                Location::getY,
                PlayerMirrorSettings::mirrorYAxis);

        RotatedLocation added = new RotatedLocation(
                new Location(blockPos.getWorld(), blockPos.getBlockX(), y, blockPos.getZ()),
                RotationAxis.Y);
        toPlaceLocations.add(added);
        return added;
    }

    public static RotatedLocation rotateZ(PlayerMirrorSettings settings, Location blockPos, List<RotatedLocation> toPlaceLocations, Boolean isXZ) {
        var z = MirrorUtils.mirrorLocationValue(settings,
                blockPos,
                Location::getZ,
                PlayerMirrorSettings::mirrorZAxis);

        RotatedLocation added = new RotatedLocation(
                new Location(blockPos.getWorld(), blockPos.getBlockX(), blockPos.getY(), z),
                isXZ ? RotationAxis.XZ : RotationAxis.Z);
        toPlaceLocations.add(added);
        return added;
    }

    public static Double mirrorLocationValue(PlayerMirrorSettings settings,
                                             Location location,
                                             Function<Location, Double> locationFetcher,
                                             Function<PlayerMirrorSettings, Boolean> settingFetcher) {

        var shouldMirror = settingFetcher.apply(settings);
        var value = locationFetcher.apply(location);
        var centerValue = locationFetcher.apply(settings.centerLocation());

        if (!shouldMirror) return value;

        return centerValue + (centerValue - value);
    }

    public static BlockFace mirrorBlockFace(PlayerMirrorSettings settings, RotationAxis rotationAxis, BlockFace face) {
        var modX = face.getModX() * (settings.mirrorXAxis() && (rotationAxis == RotationAxis.X || rotationAxis == RotationAxis.XZ) ? -1 : 0);
        var modY = face.getModY();
        var modZ = face.getModZ() * (settings.mirrorZAxis() && (rotationAxis == RotationAxis.Z || rotationAxis == RotationAxis.XZ) ? -1 : 0);

        BlockFace possibleBlockFace = findFaceWithMod(modX, modY, modZ);
        if (possibleBlockFace != null) return possibleBlockFace;

        System.out.println("Unable to rotate blockFace " + face);
        return face;
        /*
        var isX = rotationAxis == RotationAxis.X;
        if (isX) {
            System.out.println(face + "");
        }
        return face;*/
    }

    public static Stairs.Shape mirrorShape(PlayerMirrorSettings settings, RotationAxis face, Stairs.Shape shape) {
        boolean isOuterShape = shape.name().contains("OUTER");
        boolean isLeftShape = shape.name().contains("LEFT");

        if (shape == Stairs.Shape.INNER_LEFT) {
            return Stairs.Shape.INNER_RIGHT;
        } else if (shape == Stairs.Shape.INNER_RIGHT) {
            return Stairs.Shape.INNER_LEFT;
        } else if (shape == Stairs.Shape.OUTER_LEFT) {
            return Stairs.Shape.OUTER_RIGHT;
        } else if (shape == Stairs.Shape.OUTER_RIGHT) {
            return Stairs.Shape.OUTER_LEFT;
        }

        if (shape== Stairs.Shape.STRAIGHT) return shape;

        return Enum.valueOf(Stairs.Shape.class,
                (isOuterShape ? "OUTER" : "INNER") + "_" + (isLeftShape ? "LEFT" : "RIGHT"));
    }

    @Nullable
    public static BlockFace findFaceWithMod(int modX, int modY, int modZ) {
        for (BlockFace possibleBlockFace : blockFacesCache) {
            if (possibleBlockFace.getModX() == modX && possibleBlockFace.getModY() == modY && possibleBlockFace.getModZ() == modZ) {
                return possibleBlockFace;
            }
        }
        return null;
    }
}
