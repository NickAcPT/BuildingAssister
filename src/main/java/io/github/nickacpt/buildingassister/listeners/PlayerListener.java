package io.github.nickacpt.buildingassister.listeners;

import io.github.nickacpt.buildingassister.BuildingAssisterPlugin;
import io.github.nickacpt.buildingassister.display.DustParticleLineUtils;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        World world = e.getPlayer().getWorld();
        var initialLoc = new Location(world, 8.5, 6.5, 2.5);
        var finalLoc = new Location(world, 8.5, 6.5, 8.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 0.75f);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(BuildingAssisterPlugin.class), () -> {
            DustParticleLineUtils.displayLine(initialLoc.getX(), initialLoc.getY(), initialLoc.getZ(), finalLoc.getX(),
                    finalLoc.getY(), finalLoc.getZ(), world, dustOptions);
        }, 0, 5L);
    }
}
