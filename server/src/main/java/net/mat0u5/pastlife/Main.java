package net.mat0u5.pastlife;

import net.mat0u5.pastlife.lives.LivesManager;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main  {
	public static final String MOD_ID = "pastlife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static MinecraftServer server;
	public static LivesManager livesManager;

	public static void init(MinecraftServer serverInstance) {
		System.out.println("[SERVER] Initializing Past Life!");
		livesManager = new LivesManager();
		server = serverInstance;
	}

	public static void log(String message) {
		if (LOGGER != null) {
			LOGGER.info(message);
		}
		else {
			System.out.println("[Past Life] " + message);
		}
	}

	public static void error(String message) {
		if (LOGGER != null) {
			LOGGER.error(message);
		}
		else {
			System.out.println("[Past Life] " + message);
		}
	}
}
