package net.mat0u5.pastlife;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "pastlife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void init() {
		LOGGER.info("Hello Fabric world!");
	}
}