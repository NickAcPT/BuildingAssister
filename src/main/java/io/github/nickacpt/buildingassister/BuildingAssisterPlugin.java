package io.github.nickacpt.buildingassister;

import io.github.nickacpt.buildingassister.commands.MirrorCommandExecutor;
import io.github.nickacpt.buildingassister.listeners.BlockListener;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildingAssisterPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Objects.requireNonNull(Bukkit.getPluginCommand("mirror")).setExecutor(new MirrorCommandExecutor());
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
