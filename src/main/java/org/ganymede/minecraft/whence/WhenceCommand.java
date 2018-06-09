package org.ganymede.minecraft.whence;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhenceCommand implements CommandExecutor {

    WhencePlugin plugin;

    public WhenceCommand(WhencePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("whence")) {
            return false;
        }

        Player player = (Player)sender;

        Location location = player.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        WhenceWaypoint w = plugin.activeWaypoint(player.getName());

        sender.sendMessage("whence: [" + x + "," + y + "," + z + "] to [" + w.getX() + "," + w.getY() + "," + w.getZ() + "]");

        return true;
    }
}
