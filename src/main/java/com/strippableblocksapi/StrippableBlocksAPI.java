package com.strippableblocksapi;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrippableBlocksAPI implements ModInitializer {
	public static final String MOD_ID = "strippableblocksapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean isPedestalsLoaded = false;

	@Override
	public void onInitialize() {
		isPedestalsLoaded = FabricLoader.getInstance().isModLoaded("pedestals");
	}
}