package net.mat0u5.pastlife;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.mat0u5.pastlife.boogeyman.BoogeymanCommand;
import net.mat0u5.pastlife.lives.LivesCommand;
import net.mat0u5.pastlife.lives.LivesManager;
import net.mat0u5.pastlife.secretsociety.InitiateCommand;
import net.mat0u5.pastlife.secretsociety.SecretSocietyCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.handler.CommandRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main  {
	public static final String MOD_ID = "pastlife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static MinecraftServer server;
	public static LivesManager livesManager;

	public static void init(MinecraftServer serverInstance) {
		log("[SERVER] Initializing Past Life!");
		livesManager = new LivesManager();
		server = serverInstance;
		if (server.getCommandHandler() instanceof CommandRegistry) {
			CommandRegistry registry = (CommandRegistry) server.getCommandHandler();
			registry.register(new LivesCommand());
			registry.register(new BoogeymanCommand());
			registry.register(new SecretSocietyCommand());
			registry.register(new InitiateCommand());
		}
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

	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
}
