package io.github.nickacpt.buildingassister.gui;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.Orientable;
import io.github.nickacpt.buildingassister.model.MirrorAxis;
import io.github.nickacpt.buildingassister.model.PlayerMirrorSettingsStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MirrorSettingsGui extends ChestGui {
    public MirrorSettingsGui(Player viewer) {
        super(1, ComponentHolder.of(Component.text("Mirror Settings")));

        int length = 9;
        var axisListPane = new MasonryPane(length, 1);
        for (MirrorAxis axis : MirrorAxis.values()) {
            axisListPane.addPane(new BooleanTogglePane(Component.text(axis.name()),
                    new BooleanToggleFunction() {
                        @Override
                        public boolean getValue() {
                            return PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).isAxisEnabled(axis);
                        }

                        @Override
                        public void setValue(boolean val) {
                            PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).setAxisEnabled(axis, val);
                        }
                    }));
        }

        int controlsSize = 2;
        var controlsListPane = new MasonryPane(length - controlsSize, 0, controlsSize, 1);
        controlsListPane.setOrientation(Orientable.Orientation.HORIZONTAL);
        controlsListPane.addPane(new BooleanTogglePane(Component.text("Mirror Enabled"),
                new BooleanToggleFunction() {
                    @Override
                    public boolean getValue() {
                        return PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).enabled();
                    }

                    @Override
                    public void setValue(boolean val) {
                        PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).setEnabled(val);
                    }
                }));

        addPane(axisListPane);
        addPane(controlsListPane);
        setOnGlobalClick(e -> e.setCancelled(true));
    }

}
