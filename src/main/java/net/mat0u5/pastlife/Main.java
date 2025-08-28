package net.mat0u5.pastlife;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.mat0u5.pastlife.boogeyman.BoogeymanCommand;
import net.mat0u5.pastlife.lives.LivesCommand;
import net.mat0u5.pastlife.lives.LivesManager;
import net.mat0u5.pastlife.secretsociety.InitiateCommand;
import net.mat0u5.pastlife.secretsociety.SecretSocietyCommand;
import net.mat0u5.pastlife.utils.IClientHelper;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class Main implements ModInitializer {
	public static final String RESOURCEPACK_URL = "https://github.com/Mat0u5/LifeSeries-Resources/releases/download/release-pastlife-8916bc1d49a79784de7df6477810e3ff78026f2a/pastlife.zip";
	public static final String RESOURCEPACK_HASH = "6304b625874dfe7603f6e5caf4978acaffcf0132";
	public static final String MOD_ID = "pastlife";
	public static Logger LOGGER = Logger.getLogger(MOD_ID);
	public static boolean initializedCommands = false;
	public static IClientHelper clientHelper;

	public static MinecraftServer server;
	public static LivesManager livesManager;

	@Override
	public void onInitialize() {
		log("[SERVER] Initializing Past Life!");
		livesManager = new LivesManager();
		ServerLifecycleEvents.SERVER_STARTING.register(serverInstance -> server = serverInstance);
		ServerLifecycleEvents.SERVER_STARTED.register(serverInstance -> server = serverInstance);

		CommandRegistrationCallback.EVENT.register(LivesCommand::register);
		CommandRegistrationCallback.EVENT.register(BoogeymanCommand::register);
		CommandRegistrationCallback.EVENT.register(SecretSocietyCommand::register);
		CommandRegistrationCallback.EVENT.register(InitiateCommand::register);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onPlayerJoin(handler.player));
	}

	private static void onPlayerJoin(ServerPlayerEntity player) {
		MinecraftServer thisServer = player.getServer();
		if (thisServer == null) return;
		TaskScheduler.scheduleTask(2, () -> {
			applySingleResourcepack(player, RESOURCEPACK_URL, RESOURCEPACK_HASH, "Past Life Resourcepack");
		});
		TaskScheduler.scheduleTask(100, () -> {
			thisServer.getCommandManager().executeWithPrefix(thisServer.getCommandSource().withSilent(),"recipe give @a pastlife:tnt_variation");
		});
	}

	private static void applySingleResourcepack(ServerPlayerEntity player, String link, String sha1, String message) {
		UUID id = UUID.nameUUIDFromBytes(link.getBytes(StandardCharsets.UTF_8));
		ResourcePackSendS2CPacket resourcepackPacket = new ResourcePackSendS2CPacket(
				id,
				link,
				sha1,
				false,
				Optional.of(Text.translatable(message))
		);
		player.networkHandler.sendPacket(resourcepackPacket);
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
			LOGGER.warning(message);
		}
		else {
			System.out.println("[Past Life] " + message);
		}
	}

	public static void setClientHelper(IClientHelper helper) {
		clientHelper = helper;
	}

	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	public static boolean isClientPlayer(UUID uuid) {
		if (!isClient()) return false;
		return clientHelper != null && clientHelper.isMainClientPlayer(uuid);
	}
}
