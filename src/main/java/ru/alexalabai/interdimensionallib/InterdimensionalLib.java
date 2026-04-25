package ru.alexalabai.interdimensionallib;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexalabai.interdimensionallib.common.ServerCommandHandler;
import ru.alexalabai.interdimensionallib.config.ModConfig;
import ru.alexalabai.interdimensionallib.entity.ModEntities;
import ru.alexalabai.interdimensionallib.packets.ModPackets;
import ru.alexalabai.interdimensionallib.recipe.ModRecipes;

public class InterdimensionalLib implements ModInitializer {
	public static final String MOD_ID = "interdimensionallib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.INSTANCE=ModConfig.load();
		LOGGER.info("[INTERDIM]: Registered server config");
		ServerCommandHandler.regArgTypes();
		CommandRegistrationCallback.EVENT.register((dispatcher, access, env)-> ServerCommandHandler.regAll(dispatcher));
		ModPackets.regAll();
		ModEntities.regAll();
		ModRecipes.regAll();
		ServerLifecycleEvents.SERVER_STOPPED.register(s->{
			ModConfig.INSTANCE.save();
		});
		LOGGER.info("[INTERDIM]: Server initialized");
	}
}