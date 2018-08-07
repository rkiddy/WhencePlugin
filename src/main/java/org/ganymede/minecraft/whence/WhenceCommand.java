package org.ganymede.minecraft.whence;

import java.util.Arrays;
import java.util.List;

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

        if (args.length > 0 && args[0].equals("help")) {
            sender.sendMessage("/whence - give location and distance to current waypoint.");
            sender.sendMessage("/whence list - list the existing waypoints by name.");
            sender.sendMessage("/whence new a b c - create waypoint with name \"a b c\" and set current.");
            sender.sendMessage("/whence set a b c - set existing waypoint with name \"a b c\" to cs.comurrent.");
        }

        Player player = (Player)sender;

        plugin.storage.setPlayer(player);

        if (args.length == 0) {

            sender.sendMessage(this.activeWaypointMessage(player));
        }

        if (args.length > 0 && args[0].equals("set")) {

            String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

            boolean updated = plugin.storage.setActiveWaypoint(name);

            if ( ! updated) {

                sender.sendMessage("whence: ERROR setting waypoint. None set.");
            }

            WhenceWaypoint w = plugin.storage.getActiveWaypoint();

            if (w == null) {
                sender.sendMessage("whence: no active waypoint.");
            } else {
                sender.sendMessage(this.activeWaypointMessage(player));
                player.setCompassTarget(new Location(player.getWorld(), w.getDoubleX(), w.getDoubleY(), w.getDoubleZ()));
            }
        }

        if (args.length > 0 && args[0].equals("list")) {

            sender.sendMessage("whence: " + this.getWaypointNames());
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

                // the command "/whence new a b c" gives name "a b c".
                //
                String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

                w.setName(name);

                plugin.storage.createWaypoint(w);

                boolean updated = plugin.storage.setActiveWaypoint(name);

                if ( ! updated) {

                    sender.sendMessage("whence: ERROR setting waypoint. None set.");

                } else {

                    w = plugin.storage.getActiveWaypoint();

                    if (w == null) {
                        sender.sendMessage("whence: no active waypoint.");
                    } else {
                        sender.sendMessage(this.activeWaypointMessage(player));
                    }

                    player.setCompassTarget(new Location(player.getWorld(), w.getDoubleX(), w.getDoubleY(), w.getDoubleZ()));
                }
            }
        }

        if (args.length > 0 && args[0].equals("delete")) {

            String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

            boolean deleted = plugin.storage.removeWaypoint(name);

            if (deleted) {
                sender.sendMessage("whence: deleted waypoint '" + name + "'");
            } else {
                sender.sendMessage("whence: ERROR deleting waypoint '" + name + "'");
            }
        }

        return true;
    }

    private List<String> getWaypointNames() {

        List<String> waypoints = plugin.storage.getWaypointNames();

        for (int idx = 0; idx < waypoints.size(); idx++) {
            waypoints.set(idx, "'" + waypoints.get(idx) + "'");
        }

        return waypoints;
    }

    public String activeWaypointMessage(Player player) {

        WhenceWaypoint w = plugin.storage.getActiveWaypoint();

        Location location = player.getLocation();

        int x = location.getBlockX();
        int z = location.getBlockZ();

        if (w == null) {
            return "whence: no active waypoint.";
        } else {

            int distance = ((Double)(Math.sqrt(Math.pow((x-w.getX()), 2) + Math.pow((z-w.getZ()), 2)))).intValue();

            String xDir = (x < w.getX()) ? "East" : "West";

            String zDir = (z < w.getZ()) ? "South" : "North";

            return "whence: distance " + distance + " blocks, " + zDir + "-" + xDir + " to [" + w.getX() + "," + w.getZ() + "," + w.getZ() + "]";
        }
    }
}
