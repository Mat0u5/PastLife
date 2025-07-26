package net.mat0u5.pastlife;

import net.mat0u5.pastlife.lives.LivesManager;

public class Main  {

	public static LivesManager livesManager;

	public static void init() {
		System.out.println("[SERVER] Initializing Past Life!");
		livesManager = new LivesManager();
	}
}
