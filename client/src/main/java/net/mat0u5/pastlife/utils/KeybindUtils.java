package net.mat0u5.pastlife.utils;

import net.minecraft.client.Minecraft;

public class KeybindUtils {
    public static int F5State = 0;
    public static void onF5Pressed(Minecraft minecraft) {
        F5State = (F5State+1)%3;
        switch (F5State) {
            case 2:
            case 1:
                minecraft.options.debugEnabled = true;
                break;
            case 0:
            default:
                minecraft.options.debugEnabled = false;
                break;
        }
    }

    public static boolean reversedF5() {
        return F5State == 2 ;
    }

    public static boolean thirdPerson() {
        return F5State == 1 || F5State == 2;
    }
}
