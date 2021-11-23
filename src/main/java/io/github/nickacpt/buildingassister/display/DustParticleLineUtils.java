package io.github.nickacpt.buildingassister.display;

import org.bukkit.*;

public class DustParticleLineUtils {

    public static void displayLine(double initialX, double initialY, double initialZ, double finalX, double finalY, double finalZ, World world, Particle.DustOptions dustOptions) {
        displayLine(initialX, initialY, initialZ, finalX, finalY, finalZ, world, dustOptions, 10);
    }

    public static void displayLine(double initialX, double initialY, double initialZ, double finalX, double finalY, double finalZ, World world, Particle.DustOptions dustOptions, int steps) {
        double currentX = initialX;
        double currentY = initialY;
        double currentZ = initialZ;

        double stepDistanceX = ((finalX - initialX) / steps);
        double stepDistanceY = ((finalY - initialY) / steps);
        double stepDistanceZ = ((finalZ - initialZ) / steps);

        for (int step = 0; step < steps; step++) {
            world.spawnParticle(Particle.REDSTONE, currentX, currentY, currentZ, 5, stepDistanceX, stepDistanceY, stepDistanceZ, dustOptions);

            currentX += stepDistanceX;
            currentY += stepDistanceY;
            currentZ += stepDistanceZ;

        }
    }

}
