package io.github.nickacpt.buildingassister.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.component.ToggleButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BooleanTogglePane extends ToggleButton {
    public BooleanTogglePane(Component title, BooleanToggleFunction toggleFunction) {
        super(1, 1);

        if (toggleFunction.getValue()) toggle();

        setEnabledItem(createToggleItem(title, true));
        setDisabledItem(createToggleItem(title, false));

        setOnClick(e -> toggleFunction.setValue(isEnabled()));
    }

    private GuiItem createToggleItem(Component title, boolean enabled) {
        if (!enabled) title = title.replaceText(builder -> builder.matchLiteral("Enabled").replacement("Disabled"));

        var titleColor = enabled ? NamedTextColor.GREEN : NamedTextColor.RED;
        var material = enabled ? Material.LIME_DYE : Material.GRAY_DYE;

        ItemStack item = new ItemStack(material);

        Component finalTitle = title;
        item.editMeta(m -> m.displayName(finalTitle.color(titleColor).decoration(TextDecoration.ITALIC, false)));

        return new GuiItem(item);
    }
}
