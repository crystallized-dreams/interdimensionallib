package ru.alexalabai.interdimensionallib;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexalabai.interdimensionallib.common.HelperTags;
import ru.alexalabai.interdimensionallib.common.INTERDIM_ServerCommandHandler;
import ru.alexalabai.interdimensionallib.config.INTERDIM_ModConfig;
import ru.alexalabai.interdimensionallib.packets.INTERDIM_ServerPackets;
import ru.alexalabai.interdimensionallib.recipe.INTERDIM_Recipes;

public class InterdimensionalLib implements ModInitializer {
	public static final String MOD_ID = "interdimensionallib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		INTERDIM_ModConfig.INSTANCE= INTERDIM_ModConfig.load();
		LOGGER.info("[INTERDIM]: Registered server config");
		INTERDIM_ServerCommandHandler.regArgTypes();
		CommandRegistrationCallback.EVENT.register((dispatcher, access, env)-> INTERDIM_ServerCommandHandler.regAll(dispatcher));
		INTERDIM_ServerPackets.regAll();
		INTERDIM_Recipes.regAll();
		HelperTags.regAll();
		ServerLifecycleEvents.SERVER_STOPPED.register(s->{
			INTERDIM_ModConfig.INSTANCE.save();
		});
		LOGGER.info("[INTERDIM]: Server initialized");
	}
}