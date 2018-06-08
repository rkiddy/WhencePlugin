package org.ganymede.minecraft.whence;

import org.bukkit.plugin.java.JavaPlugin;

public class WhencePlugin extends JavaPlugin {
    @Override
    public void onDisable() {
    	// Nothing for now.
    }

    @Override
    public void onEnable() {
        getCommand("whence").setExecutor(new WhenceCommand(this));
    }
}
