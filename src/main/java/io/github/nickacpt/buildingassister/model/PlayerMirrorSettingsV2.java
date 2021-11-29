package io.github.nickacpt.buildingassister.model;

import java.util.EnumSet;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class PlayerMirrorSettingsV2 {
    private final EnumSet<MirrorAxis> axisToMirror = EnumSet.noneOf(MirrorAxis.class);
    private boolean enabled = false;
    @Nullable
    private Location centerLocation = null;

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAxisEnabled(MirrorAxis axis, boolean state) {
        if (state) {
            axisToMirror.add(axis);
        } else {
            axisToMirror.remove(axis);
        }
    }

    public boolean isAxisEnabled(MirrorAxis axis) {
        return axisToMirror.contains(axis);
    }

    @Nullable
    public Location centerLocation() {
        return centerLocation;
    }

    public void setCenterLocation(@Nullable Location centerLocation) {
        this.centerLocation = centerLocation;
    }

    public EnumSet<MirrorAxis> axisToMirror() {
        return axisToMirror;
    }
}
