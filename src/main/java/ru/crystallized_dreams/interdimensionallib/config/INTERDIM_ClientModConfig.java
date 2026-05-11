package ru.crystallized_dreams.interdimensionallib.config;

public class INTERDIM_ClientModConfig {
    public boolean overrideExistingLockedOptions=true;

    /// FUNCTIONALITY ///
    public static INTERDIM_ClientModConfig INSTANCE = new INTERDIM_ClientModConfig();
    public void save() {
        INTERDIM_ConfigManager.saveClient(this);}
    public static INTERDIM_ClientModConfig load() {return INTERDIM_ConfigManager.loadClient();}
}
