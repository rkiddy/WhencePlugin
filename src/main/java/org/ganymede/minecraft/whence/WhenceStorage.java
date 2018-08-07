package org.ganymede.minecraft.whence;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WhenceStorage {

    private static final String T_WAYPOINTS = "waypoints";

    private static final String COL_X = "x";
    private static final String COL_Y = "y";
    private static final String COL_Z = "z";
    private static final String COL_WORLD = "world";
    private static final String COL_PLAYER = "player";
    private static final String COL_NAME = "name";
    private static final String COL_ACTIVE = "active";

    private Plugin plugin;

    private Logger log;

    public World world;

    public Player player;

    public WhenceStorage(Plugin plugin) {
        this.plugin = plugin;
        this.log = this.plugin.getLogger();
    }

    Properties configFile;

    public String getProperty(String key) {
        return this.configFile.getProperty(key);
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.world = player.getWorld();
    }

    /*
     * Database Table Schema:
     * mysql> desc waypoints;
     * +--------+-------------+------+-----+---------+----------------+
     * | Field  | Type        | Null | Key | Default | Extra          |
     * +--------+-------------+------+-----+---------+----------------+
     * | pk     | int(11)     | NO   | PRI   | NULL  | auto_increment |
     * | x      | int(11)     | YES  |       | NULL  |                |
     * | y      | int(11)     | YES  |       | NULL  |                |
     * | z      | int(11)     | YES  |       | NULL  |                |
     * | name   | varchar(32) | YES  |       | NULL  |                |
     * | player | varchar(32) | YES  |       | NULL  |                |
     * | active | tinyint(4)  | YES  |       | NULL  |                |
     * | world  | varchar(64) | YES  |       | NULL  |                |
     * +--------+-------------+------+-----+---------+----------------+
     *
     * mysql> select * from waypoints;
     * +----+------+------+------+------+-----------+--------+-------+
     * | pk | x    | y    | z    | name | player    | active | world |
     * +----+------+------+------+------+-----------+--------+-------+
     * |  1 |    0 |    0 |    0 | zero | Arkady421 |      1 | world |
     * +----+------+------+------+------+-----------+--------+-------+
     */

    final private String username = "ray";
    final private String url = "jdbc:mysql://localhost:3306/minecrafts?autoReconnect=true&useSSL=false";

    private String password;

    static private Connection connection;

    public void setup() {
 
        configFile = new java.util.Properties();

        String cFile = System.getProperty("user.home") + "/.minecraft.txt";

        try {
            configFile.load(new FileInputStream(cFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        password = getProperty("DB_PWD");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.severe("jdbc driver unavailable!");
            return;
        }

        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void teardown() {
        try {
            if (connection != null && ! connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WhenceWaypoint getActiveWaypoint() {

        try {

            String sql = "select * from " + T_WAYPOINTS +
                    " where " +
                    COL_PLAYER + " = '" + player.getName() + "' and " +
                    COL_ACTIVE + " = 1 and " +
                    COL_WORLD + " = '" + world.getName() + "'";

            log.info(sql);

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            WhenceWaypoint w = new WhenceWaypoint();

            if ( ! rs.next()) {
                log.info("Failed");
            } else {
                w.setX(rs.getInt(COL_X));
                w.setY(rs.getInt(COL_Y));
                w.setZ(rs.getInt(COL_Z));
                w.setName(rs.getString(COL_NAME));
                w.setWorld(rs.getString(COL_WORLD));
                w.setActive(rs.getBoolean(COL_ACTIVE));
            }

            return w;

        } catch (Exception e) {
            return null;
        }
    }

    public boolean setActiveWaypoint(String name) {

        // Why is name coming out in the SQL and 'name', so I get ''name''?
        //
        name = name.replaceAll("\\'", "");

        String sql1 = "update " + T_WAYPOINTS +
                " set " + COL_ACTIVE + " = 0" +
                " where " + COL_PLAYER + " = '" + player.getName() + "'";

        log.info(sql1);

        String sql2 = "update " + T_WAYPOINTS +
                " set " + COL_ACTIVE + " = 1" +
                " where " + COL_PLAYER + " = '" + player.getName() + "'" +
                " and " + COL_NAME + " = '" + name + "'";

        log.info(sql2);

        try {

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sql1);

            stmt.executeUpdate(sql2);

        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        return true;
    }

    public void createWaypoint(WhenceWaypoint w) {

        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        columns.add(COL_X);
        values.add(String.valueOf(w.getX()));

        columns.add(COL_Y);
        values.add(String.valueOf(w.getY()));

        columns.add(COL_Z);
        values.add(String.valueOf(w.getZ()));

        columns.add(COL_WORLD);
        values.add("'" + w.getWorld() + "'");

        columns.add(COL_PLAYER);
        values.add("'" + w.getPlayer() + "'");

        columns.add(COL_NAME);
        values.add("'" + w.getName() + "'");

        columns.add(COL_ACTIVE);
        values.add(w.isActive() ? "1" : "0");

        String sql = "insert into " + T_WAYPOINTS + " (" +
                StringUtils.join(columns, ',') +
                ") values (" +
                StringUtils.join(values, ',') + ")";

        log.info(sql);

        try {

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }

    public List<String> getWaypointNames() {

        try {

            String sql = "select * from " + T_WAYPOINTS +
                    " where " +
                    COL_PLAYER + " = '" + player.getName() + "' and " +
                    COL_WORLD + " = '" + world.getName() + "'";

            log.info(sql);

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            List<String> found = new ArrayList<>();

            while (rs.next()) {
                found.add(rs.getString(COL_NAME));
            }

            return found;

        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public boolean removeWaypoint(String name) {

        String sql = "delete from " + T_WAYPOINTS +
                " where " +
                COL_NAME + " = '" + name + "'";

        log.info(sql);

        try {

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sql);

            return true;

        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }
}
