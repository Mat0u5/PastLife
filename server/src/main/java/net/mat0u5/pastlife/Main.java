package net.mat0u5.pastlife;

import net.mat0u5.pastlife.lives.LivesManager;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class Main implements ModInitializer {

	LivesManager livesManager = new LivesManager();

	@Override
	public void init() {
		System.out.println("Initializing example mod!");
	}
}
