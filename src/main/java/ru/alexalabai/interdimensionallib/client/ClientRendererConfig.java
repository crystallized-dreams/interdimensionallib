package ru.alexalabai.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientRendererConfig {
    public static class Gui {
        public static boolean shouldRenderDebug = true;
        public static boolean shouldRenderCrosshair = true;
        public static boolean shouldRenderHotbar = true;
        public static boolean shouldRenderItemText = true;
        public static boolean shouldRenderOverlayMessage = true;
        public static boolean shouldRenderTitleAndSubtitle = true;
        public static boolean shouldRenderStatusEffectOverlay = true;
        public static boolean shouldRenderExperienceBar = true;
        public static boolean shouldRenderHealth = true;
        public static boolean shouldRenderArmor = true;
        public static boolean shouldRenderAir = true;
        public static boolean shouldRenderMountHealth = true;
        public static boolean shouldRenderFood = true;
        public static boolean shouldRenderScoreboardSidebar = true;
        public static boolean shouldRenderBossbars = true;
        public static boolean shouldRenderChat = true;
        public static boolean shouldRenderPlayerList = true;
        public static boolean shouldRenderMisc = true;

        public static void setAll(boolean state) {
            shouldRenderDebug = state;
            shouldRenderCrosshair = state;
            shouldRenderHotbar = state;
            shouldRenderItemText = state;
            shouldRenderOverlayMessage = state;
            shouldRenderTitleAndSubtitle = state;
            shouldRenderStatusEffectOverlay = state;
            shouldRenderExperienceBar = state;
            shouldRenderHealth = state;
            shouldRenderArmor = state;
            shouldRenderAir = state;
            shouldRenderMountHealth = state;
            shouldRenderFood = state;
            shouldRenderScoreboardSidebar = state;
            shouldRenderBossbars = state;
            shouldRenderChat = state;
            shouldRenderPlayerList = state;
            shouldRenderMisc = state;
        }
    }
    public static FogAccessor.FogData FOG;
    public static boolean overrideFog=false;
}
