package io.github.nickacpt.buildingassister.listeners;

import java.util.Objects;
import org.bukkit.Location;

public record RotatedLocation(Location location, RotationAxis face) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RotatedLocation that = (RotatedLocation) o;

        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }
}
