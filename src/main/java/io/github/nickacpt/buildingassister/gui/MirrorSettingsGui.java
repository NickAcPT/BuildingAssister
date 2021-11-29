package io.github.nickacpt.buildingassister.gui;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.*;
import io.github.nickacpt.buildingassister.model.*;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MirrorSettingsGui extends ChestGui {
    public MirrorSettingsGui(Player viewer) {
        super(1, ComponentHolder.of(Component.text("Mirror Settings")));

        int length = 9;
        var axisListPane = new MasonryPane(length, 1);
        for (MirrorAxis axis : MirrorAxis.values()) {
            axisListPane.addPane(new BooleanTogglePane(Component.text(axis.name()),
                    new MirrorAxisSettingsBooleanToggle(viewer, axis)));
        }

        int controlsSize = 2;
        var controlsListPane = new MasonryPane(length - controlsSize, 0, controlsSize, 1);
        controlsListPane.setOrientation(Orientable.Orientation.HORIZONTAL);
        controlsListPane.addPane(new BooleanTogglePane(Component.text("Mirror Enabled"), new BooleanToggleFunction() {
            @Override
            public boolean getValue() {
                return PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).enabled();
            }

            @Override
            public void setValue(boolean val) {
                PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer).setEnabled(val);
            }
        }));

        StaticPane centerLocationPane = new StaticPane(1, 1);
        addCenterLocationGuiItem(viewer, centerLocationPane);
        controlsListPane.addPane(centerLocationPane);

        addPane(axisListPane);
        addPane(controlsListPane);
        setOnGlobalClick(e -> e.setCancelled(true));
    }

    private void addCenterLocationGuiItem(Player viewer, StaticPane centerLocationPane) {
        centerLocationPane.addItem(new GuiItem(getCenterItemStack(viewer), e -> {
            PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer)
                    .setCenterLocation(viewer.getLocation().toCenterLocation());
            centerLocationPane.removeItem(0, 0);
            addCenterLocationGuiItem(viewer, centerLocationPane);
            update();
        }), 0, 0);
    }

    private ItemStack getCenterItemStack(Player viewer) {
        PlayerMirrorSettingsV2 viewerSettings = PlayerMirrorSettingsStorage.getPlayerMirrorSettingsV2(viewer);
        ItemStack stack = new ItemStack(Material.TARGET);
        stack.editMeta(meta -> {
            meta.displayName(
                    Component.text("Center Position").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

            Component locationText = Component.text("Not set").color(NamedTextColor.RED);
            Location centerLocation = viewerSettings.centerLocation();
            if (centerLocation != null) {
                locationText = Component.text(getLocationAsText(centerLocation)).color(NamedTextColor.GREEN);
            }
            meta.lore(List.of(locationText));
        });
        return stack;
    }

    @NotNull
    private String getLocationAsText(Location centerLocation) {
        return centerLocation.getBlockX() + ", " + centerLocation.getBlockY() + ", " + centerLocation.getBlockZ();
    }

}
