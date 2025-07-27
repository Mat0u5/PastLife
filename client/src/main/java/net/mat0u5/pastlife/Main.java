package net.mat0u5.pastlife;

import net.mat0u5.pastlife.utils.ResourceHandler;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.nio.file.Path;

public class Main {
	public static Minecraft minecraft;

	public static void init(Minecraft minecraftInstance) {
		System.out.println("[CLIENT] Initializing Past Life!");
		minecraft = minecraftInstance;


		File runDirectory = minecraft.getRunDirectory();
		Path runPath = runDirectory.toPath();
		Path lifeseriesResourcesPath = runPath.resolve("resources/lifeseries");
		lifeseriesResourcesPath.toFile().mkdirs();
		ResourceHandler handler = new ResourceHandler();
		handler.copyBundledSingleFile("/assets/boogeyman_wait.ogg", lifeseriesResourcesPath.resolve("boogeyman_wait.ogg"));
		handler.copyBundledSingleFile("/assets/boogeyman_yes.ogg", lifeseriesResourcesPath.resolve("boogeyman_yes.ogg"));
		handler.copyBundledSingleFile("/assets/boogeyman_no.ogg", lifeseriesResourcesPath.resolve("boogeyman_no.ogg"));

		minecraft.soundSystem.loadSound("boogeyman_wait.ogg", lifeseriesResourcesPath.resolve("boogeyman_wait.ogg").toFile());
		minecraft.soundSystem.loadSound("boogeyman_yes.ogg", lifeseriesResourcesPath.resolve("boogeyman_yes.ogg").toFile());
		minecraft.soundSystem.loadSound("boogeyman_no.ogg", lifeseriesResourcesPath.resolve("boogeyman_no.ogg").toFile());
	}
}
