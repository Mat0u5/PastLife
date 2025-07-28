package net.mat0u5.pastlife;

import net.mat0u5.pastlife.utils.ResourceHandler;
import net.mat0u5.pastlife.utils.TitleRenderer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

public class Main {
	public static final String MOD_ID = "pastlife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Minecraft minecraft;
	public static TitleRenderer titleRenderer;

	public static void init(Minecraft minecraftInstance) {
		log("[CLIENT] Initializing Past Life!");
		minecraft = minecraftInstance;
		titleRenderer = new TitleRenderer();


		File runDirectory = minecraft.getRunDirectory();
		Path runPath = runDirectory.toPath();
		Path lifeseriesResourcesPath = runPath.resolve("resources/lifeseries");
		lifeseriesResourcesPath.toFile().mkdirs();
		ResourceHandler handler = new ResourceHandler();
		handler.copyBundledSingleFile("/assets/boogeyman_wait.ogg", lifeseriesResourcesPath.resolve("boogeyman_wait.ogg"));
		handler.copyBundledSingleFile("/assets/boogeyman_yes.ogg", lifeseriesResourcesPath.resolve("boogeyman_yes.ogg"));
		handler.copyBundledSingleFile("/assets/boogeyman_no.ogg", lifeseriesResourcesPath.resolve("boogeyman_no.ogg"));
		handler.copyBundledSingleFile("/assets/boogeyman_cure.ogg", lifeseriesResourcesPath.resolve("boogeyman_cure.ogg"));
		handler.copyBundledSingleFile("/assets/boogeyman_fail.ogg", lifeseriesResourcesPath.resolve("boogeyman_fail.ogg"));
		handler.copyBundledSingleFile("/assets/didgeridoo.ogg", lifeseriesResourcesPath.resolve("didgeridoo.ogg"));

		minecraft.soundSystem.loadSound("boogeyman_wait.ogg", lifeseriesResourcesPath.resolve("boogeyman_wait.ogg").toFile());
		minecraft.soundSystem.loadSound("boogeyman_yes.ogg", lifeseriesResourcesPath.resolve("boogeyman_yes.ogg").toFile());
		minecraft.soundSystem.loadSound("boogeyman_no.ogg", lifeseriesResourcesPath.resolve("boogeyman_no.ogg").toFile());
		minecraft.soundSystem.loadSound("boogeyman_cure.ogg", lifeseriesResourcesPath.resolve("boogeyman_cure.ogg").toFile());
		minecraft.soundSystem.loadSound("boogeyman_fail.ogg", lifeseriesResourcesPath.resolve("boogeyman_fail.ogg").toFile());
		minecraft.soundSystem.loadSound("didgeridoo.ogg", lifeseriesResourcesPath.resolve("didgeridoo.ogg").toFile());
	}

	public static void log(String message) {
		if (LOGGER != null) {
			LOGGER.info("[Past Life] " + message);
		}
		else {
			System.out.println("[Past Life] " + message);
		}
	}

	public static void error(String message) {
		if (LOGGER != null) {
			LOGGER.error("[Past Life] " + message);
		}
		else {
			System.out.println("[Past Life] " + message);
		}
	}
}
