package io.github.nickacpt.buildingassister.model;

import org.bukkit.Location;

public final class PlayerMirrorSettings {
    public Boolean rotateXAxis() {
        return rotateXAxis;
    }

    public void setRotateXAxis(Boolean rotateXAxis) {
        this.rotateXAxis = rotateXAxis;
    }

    public Boolean rotateYAxis() {
        return rotateYAxis;
    }

    public void setRotateYAxis(Boolean rotateYAxis) {
        this.rotateYAxis = rotateYAxis;
    }

    public Boolean rotateZAxis() {
        return rotateZAxis;
    }

    public void setRotateZAxis(Boolean rotateZAxis) {
        this.rotateZAxis = rotateZAxis;
    }

    public Boolean mirrorXAxis() {
        return mirrorXAxis;
    }

    public void setMirrorXAxis(Boolean mirrorXAxis) {
        this.mirrorXAxis = mirrorXAxis;
    }

    public Boolean mirrorYAxis() {
        return mirrorYAxis;
    }

    public void setMirrorYAxis(Boolean mirrorYAxis) {
        this.mirrorYAxis = mirrorYAxis;
    }

    public Boolean mirrorZAxis() {
        return mirrorZAxis;
    }

    public void setMirrorZAxis(Boolean mirrorZAxis) {
        this.mirrorZAxis = mirrorZAxis;
    }

    public Location centerLocation() {
        return centerLocation;
    }

    public void setCenterLocation(Location centerLocation) {
        this.centerLocation = centerLocation;
    }

    private Boolean rotateXAxis;
    private Boolean rotateYAxis;
    private Boolean rotateZAxis;
    private Boolean mirrorXAxis;
    private Boolean mirrorYAxis;
    private Boolean mirrorZAxis;
    private Location centerLocation;

    public PlayerMirrorSettings(Location centerLocation, Boolean rotateXAxis, Boolean rotateYAxis, Boolean rotateZAxis, Boolean mirrorXAxis, Boolean mirrorYAxis, Boolean mirrorZAxis) {
        this.rotateXAxis = rotateXAxis;
        this.rotateYAxis = rotateYAxis;
        this.rotateZAxis = rotateZAxis;
        this.centerLocation = centerLocation;
        this.mirrorXAxis = mirrorXAxis;
        this.mirrorYAxis = mirrorYAxis;
        this.mirrorZAxis = mirrorZAxis;
    }

}
