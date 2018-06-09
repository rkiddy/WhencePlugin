package org.ganymede.minecraft.whence;

public class WhenceWaypoint {

    /*
     * mysql> select * from waypoints;
     * +----+------+------+------+------+-----------+--------+
     * | pk | x    | y    | z    | name | player    | active |
     * +----+------+------+------+------+-----------+--------+
     * |  1 |    0 |    0 |    0 | zero | Arkady421 |      1 |
     * +----+------+------+------+------+-----------+--------+
     */

    private int x;
    private int y;
    private int z;

    private String name;
    private String player;
    private String world;

    private boolean active;

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }
    public void setZ(int z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }

    public String getWorld() {
        return world;
    }
    public void setWorld(String world) {
        this.world = world;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
