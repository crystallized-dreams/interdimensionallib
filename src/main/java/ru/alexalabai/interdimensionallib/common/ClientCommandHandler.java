package ru.alexalabai.interdimensionallib.common;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.alexalabai.interdimensionallib.InterdimensionalLibClient;
import ru.alexalabai.interdimensionallib.packets.all.MovementStatePayload;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class ClientCommandHandler {
    public static void regAll(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("crawl").executes(ctx->{
            if(!InterdimensionalLibClient.isCrawlingAllowedOnServer) {
                ctx.getSource().getPlayer().sendMessage(Text.translatable("text.interdimensionallib.crawling_disallowed"));
                return 0;
            }
            InterdimensionalLibClient.activelySitting=false;
            InterdimensionalLibClient.activelyCrawlingForceFromCommand=true;
            InterdimensionalLibClient.activelyCrawling=!InterdimensionalLibClient.activelyCrawling;
            return 1;
        }));
        dispatcher.register(literal("sit").executes(ctx->{
            if(InterdimensionalLibClient.isSittingAllowedOnServer) {
                InterdimensionalLibClient.activelySitting = !InterdimensionalLibClient.activelySitting;
                InterdimensionalLibClient.activelyCrawling = false;
                ClientPlayNetworking.send(new MovementStatePayload(InterdimensionalLibClient.activelySitting ? 1 : 0));
            } else MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.interdimensionallib.sitting_disallowed"));
            return 1;
        }));
    }
}
