package ru.alexalabai.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientRendererConfig {
    public static class Gui {
        public static final boolean shouldRenderDebug = true;
        public static final boolean shouldRenderCrosshair = true;
        public static final boolean shouldRenderHotbar = true;
        public static final boolean shouldRenderStatusEffectOverlay = true;
        public static final boolean shouldRenderExperienceBar = true;
        public static final boolean shouldRenderItemText = true;
        public static final boolean shouldRenderHealth = true;
        public static final boolean shouldRenderArmor = true;
        public static final boolean shouldRenderAir = true;
        public static final boolean shouldRenderMountHealth = true;
        public static final boolean shouldRenderFood = true;
        public static final boolean shouldRenderOverlayMessage = true;
        public static final boolean shouldRenderTitleAndSubtitle = true;
        public static final boolean shouldRenderScoreboardSidebar = true;
        public static final boolean shouldRenderBossbars = true;
        public static final boolean shouldRenderChat = true;
        public static final boolean shouldRenderPlayerList = true;
        public static final boolean shouldRenderMisc = true;
    }
}
