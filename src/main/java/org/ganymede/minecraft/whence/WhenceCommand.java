package org.ganymede.minecraft.whence;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
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

        sender.sendMessage("whence args: " +StringUtils.join(args, ' '));

        if (args.length == 0) {

            WhenceWaypoint w = plugin.storage.getActiveWaypoint(player.getName(), player.getWorld().getName());

            if (w == null) {
                sender.sendMessage("whence: [" + x + "," + y + "," + z + "] to [NOT FOUND]");
            } else {
                sender.sendMessage("whence: [" + x + "," + y + "," + z + "] to [" + w.getX() + "," + w.getY() + "," + w.getZ() + "]");
            }
        }

        if (args.length > 0 && args[0].equals("new")) {

            if (args.length == 1) {
                sender.sendMessage("whence: \"new\" command needs a name after.");
                sender.sendMessage("whence: The name may contains spaces.");
            } else {

                WhenceWaypoint w = new WhenceWaypoint();

                w.setPlayer(player.getName());
                w.setWorld(player.getWorld().getName());

                Location loc = player.getLocation();

                w.setX(loc.getBlockX());
                w.setY(loc.getBlockY());
                w.setZ(loc.getBlockZ());

                w.setActive(false);

                String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

                w.setName(name);

                plugin.storage.createWaypoint(w);
            }
        }

        return true;
    }
}
