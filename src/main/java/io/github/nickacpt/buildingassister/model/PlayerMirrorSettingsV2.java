package io.github.nickacpt.buildingassister.model;

import com.destroystokyo.paper.util.set.OptimizedSmallEnumSet;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class PlayerMirrorSettingsV2 {
    private final OptimizedSmallEnumSet<MirrorAxis> axisToMirror = new OptimizedSmallEnumSet<>(MirrorAxis.class);
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
            axisToMirror.addUnchecked(axis);
        } else {
            axisToMirror.removeUnchecked(axis);
        }
    }

    public boolean isAxisEnabled(MirrorAxis axis) {
        return axisToMirror.hasElement(axis);
    }

    @Nullable
    public Location centerLocation() {
        return centerLocation;
    }

    public void setCenterLocation(@Nullable Location centerLocation) {
        this.centerLocation = centerLocation;
    }

    public OptimizedSmallEnumSet<MirrorAxis> axisToMirror() {
        return axisToMirror;
    }
}
