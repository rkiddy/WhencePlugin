package org.ganymede.minecraft.whence;

import java.io.FileInputStream;
import java.util.Properties;

import org.bukkit.plugin.java.JavaPlugin;

public class WhencePlugin extends JavaPlugin {

    final WhenceStorage storage = new WhenceStorage(this);

    @Override
    public void onEnable() {
        getCommand("whence").setExecutor(new WhenceCommand(this));
        this.setup();
        storage.setup();
    }

    @Override
    public void onDisable() {
        storage.teardown();
    }

    Properties configFile;

    public String getProperty(String key) {
        return this.configFile.getProperty(key);
    }

    public String debug;

    public void dprint(String msg) {
        if ("TRUE".equalsIgnoreCase(debug)) {
            System.out.println("DEBUG: " + msg);
        } else {
            System.out.println("DEBUG is false");
        }
    }

    public void setup() {

        configFile = new java.util.Properties();

        String cFile = System.getProperty("user.home") + "/.minecraft.txt";

        try {
            configFile.load(new FileInputStream(cFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        debug = getProperty("DEBUG");
    }
}
