package org.ganymede.minecraft.whence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhenceCommand implements CommandExecutor {

    WhencePlugin plugin;

    void dprint(String msg) {
        if ("TRUE".equalsIgnoreCase(this.plugin.debug)) {
            System.out.println("DEBUG: " + msg);
        } else {
            System.out.println("DEBUG is false");
        }
    }

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

        plugin.storage.setPlayer(player);

        if (args.length > 0 && args[0].equals("help")) {
            sender.sendMessage("/whence - give location and distance to current waypoint.");
            sender.sendMessage("/whence help - this list of commands.");
            sender.sendMessage("/whence list - list the existing waypoints by name.");
            sender.sendMessage("/whence new a b c - create waypoint with name \"a b c\" and set current.");
            sender.sendMessage("/whence set a b c - set existing waypoint with name \"a b c\" to current.");
            sender.sendMessage("/whence delete a b c - delete waypoint with name \"a b c\".");
            sender.sendMessage("/whence to x y a b c - create waypoint with name \"a b c\" at location (x, 62, y).");
        }

        if (args.length == 0) {

            sender.sendMessage(this.activeWaypointMessage(player));
        }

        if (args.length > 0 && args[0].equals("set")) {

            dprint("command: set");

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

            dprint("command: list");

            sender.sendMessage("whence: " + this.getWaypointNames());
        }

        if (args.length > 0 && args[0].equals("new")) {

            dprint("command: new");

            if (args.length == 1) {
                sender.sendMessage("whence: \"new\" command needs a name after.");
                sender.sendMessage("whence: The name may contains spaces.");
            } else {

                WhenceWaypoint w = new WhenceWaypoint();

                w.setPlayer(player.getName());
                w.setWorld(player.getWorld().getName());

                Location loc = player.getLocation();

                dprint("where? " + loc);

                w.setX(loc.getBlockX());
                w.setY(loc.getBlockY());
                w.setZ(loc.getBlockZ());

                w.setActive(false);

                // the command "/whence new a b c" gives name "a b c".
                //
                String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

                w.setName(name);

                dprint(w.toString());

                plugin.storage.saveWaypoint(w);

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

            dprint("command: delete");

            String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

            WhenceWaypoint w = plugin.storage.getActiveWaypoint();

            if (w.getName().equals(name)) {

                plugin.storage.setActiveWaypoint(null);

                player.setCompassTarget(new Location(player.getWorld(), 0d, 0d, 0d));
            }

            boolean deleted = plugin.storage.removeWaypoint(name);

            if (deleted) {
                sender.sendMessage("whence: deleted waypoint '" + name + "'");
            } else {
                sender.sendMessage("whence: ERROR deleting waypoint '" + name + "'");
            }
        }

        if (args.length > 0 && args[0].equals("to")) {

            dprint("command: to");

            if (args.length < 4) {
                sender.sendMessage("whence: \"to\" command needs a x, a y, and name after.");
                sender.sendMessage("whence: The name may contains spaces.");
            } else {

                int xLoc = Integer.valueOf(args[1]);
                int zLoc = Integer.valueOf(args[2]);

                WhenceWaypoint w = new WhenceWaypoint();

                w.setPlayer(player.getName());
                w.setWorld(player.getWorld().getName());

                Location loc = player.getLocation();

                dprint("where? " + loc);

                w.setX(xLoc);
                w.setY(62);
                w.setZ(zLoc);

                w.setActive(false);

                // the command "/whence new a b c" gives name "a b c".
                //
                String name = StringUtils.join(Arrays.copyOfRange(args, 3, args.length), ' ');

                w.setName(name);

                dprint(w.toString());

                plugin.storage.saveWaypoint(w);

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

        return true;
    }

    private List<String> getWaypointNames() {

        Map<Integer,List<String>> waypoints = plugin.storage.listWaypoints();

        List<String> nextWays = new ArrayList<>();

        List<Integer> distances = new ArrayList<Integer>(waypoints.keySet());

        Collections.sort(distances);

        for (Integer distance : distances) {

            for (String name : waypoints.get(distance)) {

                nextWays.add("\"" + name + "\" (" + distance + ")");
            }
        }

        return nextWays;
    }

    public String activeWaypointMessage(Player player) {

        WhenceWaypoint w = plugin.storage.getActiveWaypoint();

        dprint("active: " + w);

        if (w.getName() == null || "null".equals(w.getName())) {

            return "whence: NO waypoint is active.";

        } else {

            Location location = player.getLocation();

            int x = location.getBlockX();
            int z = location.getBlockZ();

            int distance = ((Double)(Math.sqrt(Math.pow((x-w.getX()), 2) + Math.pow((z-w.getZ()), 2)))).intValue();

            String xDir = (x < w.getX()) ? "East" : "West";

            String zDir = (z < w.getZ()) ? "South" : "North";

            return "whence: " + distance + " blocks, " + zDir + "-" + xDir + " to [" + w.getX() + "," + w.getY() + "," + w.getZ() + "]: '" + w.getName() + "'";
        }
    }
}
