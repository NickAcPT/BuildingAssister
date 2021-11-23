package io.github.nickacpt.buildingassister;

import io.github.nickacpt.buildingassister.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildingAssisterPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
