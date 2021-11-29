package io.github.nickacpt.buildingassister.gui;

import io.github.nickacpt.buildingassister.model.MirrorAxis;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import org.bukkit.entity.Player;

public class MirrorAxisSettingsBooleanToggle implements BooleanToggleFunction {

    private final Player viewer;
    private final MirrorAxis axis;

    public MirrorAxisSettingsBooleanToggle(Player viewer, MirrorAxis axis) {
        this.viewer = viewer;
        this.axis = axis;
    }

    @Override
    public boolean getValue() {
        return PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).isAxisEnabled(axis);
    }

    @Override
    public void setValue(boolean val) {
        PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).setAxisEnabled(axis, val);
    }
}
