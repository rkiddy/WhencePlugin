package org.ganymede.minecraft.whence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class WhencePlugin extends JavaPlugin {

    static private Connection connection;

    /*
     * Database Table Schema:
     * mysql> desc waypoints;
     * +--------+-------------+------+-----+---------+----------------+
     * | Field  | Type        | Null | Key | Default | Extra          |
     * +--------+-------------+------+-----+---------+----------------+
     * | pk     | int(11)     | NO   | PRI | NULL    | auto_increment |
     * | x      | int(11)     | YES  |     | NULL    |                |
     * | y      | int(11)     | YES  |     | NULL    |                |
     * | z      | int(11)     | YES  |     | NULL    |                |
     * | name   | varchar(32) | YES  |     | NULL    |                |
     * | player | varchar(32) | YES  |     | NULL    |                |
     * | active | tinyint(4)  | YES  |     | NULL    |                |
     * +--------+-------------+------+-----+---------+----------------+
     *
     * mysql> select * from waypoints;
     * +----+------+------+------+------+-----------+--------+
     * | pk | x    | y    | z    | name | player    | active |
     * +----+------+------+------+------+-----------+--------+
     * |  1 |    0 |    0 |    0 | zero | Arkady421 |      1 |
     * +----+------+------+------+------+-----------+--------+
     */

    final private String username="ray";
    final private String password="alexna11";
    final private String url = "jdbc:mysql://localhost:3306/minecrafts";

    final private Logger log = this.getLogger();

    @Override
    public void onEnable() {

        System.err.println("WhencePlugin::onEnable START");

        getCommand("whence").setExecutor(new WhenceCommand(this));

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }

        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.err.println("WhencePlugin::onEnable DONE");
    }

    @Override
    public void onDisable() {

        log.info("WhencePlugin::onDisable DONE");

        try {
            if (connection != null && ! connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("WhencePlugin::onDisable DONE");
    }

    public WhenceWaypoint activeWaypoint(String playerName) {

        try {

            String sql = "select * from waypoints where player = '" + playerName + "' and active = 1";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            WhenceWaypoint w = new WhenceWaypoint();

            if ( ! rs.next()) {
                log.info("Failed");
            } else {
                w.setX(rs.getInt("x"));
                w.setY(rs.getInt("y"));
                w.setZ(rs.getInt("z"));
                w.setName(rs.getString("name"));
                log.info("Success");
            }

            return w;

        } catch (Exception e) {
            return null;
        }
    }
}
