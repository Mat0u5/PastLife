package net.mat0u5.pastlife;

import net.mat0u5.pastlife.lives.LivesManager;
import net.minecraft.server.MinecraftServer;

public class Main  {

	public static MinecraftServer server;
	public static LivesManager livesManager;

	public static void init(MinecraftServer serverInstance) {
		System.out.println("[SERVER] Initializing Past Life!");
		livesManager = new LivesManager();
		server = serverInstance;
	}
}
