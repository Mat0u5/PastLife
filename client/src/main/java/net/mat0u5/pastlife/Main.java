package net.mat0u5.pastlife;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	public static final String MOD_ID = "pastlife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void init() {
		log("[CLIENT] Initializing Past Life!");
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
