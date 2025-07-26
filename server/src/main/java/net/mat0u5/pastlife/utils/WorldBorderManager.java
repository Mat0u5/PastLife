package net.mat0u5.pastlife.utils;

public class WorldBorderManager {
    private static int borderSize = 400;

    public static int getSize() {
        return borderSize;
    }

    public static boolean isOutsideBorder(double centerX, double centerZ, double x, double z) {
        double halfSize = borderSize / 2.0;
        return Math.abs(x - centerX) > halfSize || Math.abs(z - centerZ) > halfSize;
    }
}
