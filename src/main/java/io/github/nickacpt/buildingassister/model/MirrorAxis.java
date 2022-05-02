package io.github.nickacpt.buildingassister.model;

public enum MirrorAxis {
    X,
    Y,
    Z,
    XZ {
        @Override
        public MirrorAxis getLeadingAxis() {
            return X;
        }
    },
    ZX {
        @Override
        public MirrorAxis getLeadingAxis() {
            return Z;
        }
    };

    public static MirrorAxis[] getMainAxis() {
        return new MirrorAxis[]{X, Y, Z};
    }

    public MirrorAxis getLeadingAxis() {
        return this;
    }
}
