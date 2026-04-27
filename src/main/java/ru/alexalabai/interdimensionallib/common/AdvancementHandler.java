package ru.alexalabai.interdimensionallib.common;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementHandler {
    public static void regAll() { }

    public static void grant(ServerPlayerEntity player, Identifier advancement, String criterion) {
        AdvancementEntry adv=player.getServer().getAdvancementLoader().get(advancement);
        if(adv==null) return;
        if(!player.getAdvancementTracker().getProgress(adv).isDone())
            player.getAdvancementTracker().grantCriterion(adv,criterion);
    }
    public static void grant(ServerPlayerEntity player, Identifier advancement) {
        AdvancementEntry adv=player.getServer().getAdvancementLoader().get(advancement);
        if(adv==null) return;
        if(!player.getAdvancementTracker().getProgress(adv).isDone()) {
            for(String criterion : player.getAdvancementTracker().getProgress(adv).getUnobtainedCriteria())
                player.getAdvancementTracker().grantCriterion(adv,criterion);
        }
    }
    public static void revoke(ServerPlayerEntity player, Identifier advancement, String criterion) {
        AdvancementEntry adv=player.getServer().getAdvancementLoader().get(advancement);
        if(adv==null) return;
        player.getAdvancementTracker().revokeCriterion(adv,criterion);
    }
    public static void revoke(ServerPlayerEntity player, Identifier advancement) {
        AdvancementEntry adv=player.getServer().getAdvancementLoader().get(advancement);
        if(adv==null) return;
        for(String criterion : player.getAdvancementTracker().getProgress(adv).getObtainedCriteria())
            player.getAdvancementTracker().revokeCriterion(adv,criterion);
    }
}
