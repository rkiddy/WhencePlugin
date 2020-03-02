package org.ganymede.minecraft.whence;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class WhenceEventHandler implements Listener, EventExecutor {

    private WhenceStorage storage;

    public void setStorage(WhenceStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        storage.savePlayerDeathLocation();
    }
}
