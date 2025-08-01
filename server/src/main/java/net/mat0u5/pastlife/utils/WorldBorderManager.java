package net.mat0u5.pastlife.utils;

import net.mat0u5.pastlife.Main;

public class WorldBorderManager {
    public static int borderSize = 0;
    public static int centerX = 0;
    public static int centerZ = 0;
    public static boolean initialized = false;

    public static void init(int size, int centerX, int centerZ) {
        WorldBorderManager.borderSize = size;
        WorldBorderManager.centerX = centerX;
        WorldBorderManager.centerZ = centerZ;
        if (!initialized) {
            Main.log("[SERVER] World Border initialized with size: " + size + ", centerX: " + centerX + ", centerZ: " + centerZ);
        }
        initialized = true;
    }

    public static boolean isOutsideBorder(double x, double z) {
        double halfSize = borderSize / 2.0;
        return Math.abs(x - centerX) > halfSize || Math.abs(z - centerZ) > halfSize;
    }
}
