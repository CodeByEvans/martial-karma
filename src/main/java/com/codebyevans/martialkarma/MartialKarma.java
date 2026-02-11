package com.codebyevans.martialkarma;

import com.codebyevans.martialkarma.ability.HeartMarkTracker;
import com.codebyevans.martialkarma.command.AbilityCommand;
import com.codebyevans.martialkarma.entity.ModEntities;
import com.codebyevans.martialkarma.networking.HeartMarkPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MartialKarma implements ModInitializer {
	public static final String MOD_ID = "martial-karma";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModCommands.registerCommands();
		ModEntities.register();
		HeartMarkPacket.register();

		// Registrar comando
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			AbilityCommand.register(dispatcher, registryAccess, environment);
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			HeartMarkTracker.tick(world);
		});

		LOGGER.info("Hello Fabric world!");
	}
}