package ru.crystallized_dreams.interdimensionallib.config;

public class INTERDIM_ModConfig {
    public boolean overhaulBlockInteractions = true;

    /// FUNCTIONALITY ///
    public static INTERDIM_ModConfig INSTANCE = new INTERDIM_ModConfig();
    public void save() {
        INTERDIM_ConfigManager.save(this);}
    public static INTERDIM_ModConfig load() {return INTERDIM_ConfigManager.load();}
}
