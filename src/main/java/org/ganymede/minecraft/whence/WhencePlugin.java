package org.ganymede.minecraft.whence;

import org.bukkit.plugin.java.JavaPlugin;

public class WhencePlugin extends JavaPlugin {

    final WhenceStorage storage = new WhenceStorage(this);

    @Override
    public void onEnable() {
	getCommand("whence").setExecutor(new WhenceCommand(this));
	storage.setup();
    }

    @Override
    public void onDisable() {
	storage.teardown();
    }
}
