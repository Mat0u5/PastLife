package net.mat0u5.pastlife;

import net.minecraft.client.Minecraft;

public class Main {
	public static Minecraft minecraft;

	public static void init(Minecraft minecraftInstance) {
		System.out.println("[CLIENT] Initializing Past Life!");
		minecraft = minecraftInstance;
	}
}
