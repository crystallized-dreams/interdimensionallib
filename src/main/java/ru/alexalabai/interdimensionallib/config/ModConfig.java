package ru.alexalabai.interdimensionallib.config;

public class ModConfig {
    public boolean overhaulBlockInteractions = true;

    /// FUNCTIONALITY ///
    public static ModConfig INSTANCE = new ModConfig();
    public void save() {ConfigManager.save(this);}
    public static ModConfig load() {return ConfigManager.load();}
}
