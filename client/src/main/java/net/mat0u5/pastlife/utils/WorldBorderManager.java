package net.mat0u5.pastlife.utils;

public class WorldBorderManager {
    private static int borderSize = 400;
    private static double centerX = 0.0;
    private static double centerZ = 0.0;
    public static boolean setUp = false;

    public static void setSize(int size) {
        borderSize = size;
    }

    public static int getSize() {
        return borderSize;
    }

    public static void setCenter(double x, double z) {
        centerX = x;
        centerZ = z;
        setUp = true;
    }

    public static double getCenterX() {
        return centerX;
    }

    public static double getCenterZ() {
        return centerZ;
    }

    public static boolean isOutsideBorder(double x, double z) {
        double halfSize = borderSize / 2.0;
        return Math.abs(x - centerX) > halfSize || Math.abs(z - centerZ) > halfSize;
    }

    public static double getDistanceOutside(double x, double z) {
        double halfSize = borderSize / 2.0;
        double distX = Math.max(0, Math.abs(x - centerX) - halfSize);
        double distZ = Math.max(0, Math.abs(z - centerZ) - halfSize);
        return Math.sqrt(distX * distX + distZ * distZ);
    }
}
