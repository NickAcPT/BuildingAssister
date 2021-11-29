package io.github.nickacpt.buildingassister.model;

public enum MirrorAxis {
    X,
    Y,
    Z,
    XZ,
    ZX;

    public static MirrorAxis[] getMainAxis() {
        return new MirrorAxis[] {X, Y, Z};
    }
}
