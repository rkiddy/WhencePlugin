# WhencePlugin

The idea here is simple. I often get lost when moving around my fairly large Minecraft world. And
I like to run in large worlds, and not just kick off a series of small ones.

Unfortunately, just building big pylons does not help you past the immediate horizon.

Waypoints have a name, a location (and so x, y, and z) and are specific to a player and world.

The table also contains one row with the 'active' column set to 1. All other rows are set to 0.

When one creates a new waypoint and it becomes active, or when one activates an existing waypoint,
a compass will point to it so that you can find it without getting lost.

A few rows from the waypoints table might look like this:

pk | x | y | z | name | player | active | world
--- | --- | --- | --- | --- | --- | --- | ---
1 | 0 | 0 | 0 | zero | Arkady421 | 0 | world
2 | 1000 | 64 | 1000 | ocean monument | Arkady421 | 1 | world

The commands offered by this plugin are:

* /whence - give location and distance to current waypoint.
* /whence help - this list fo commands.
* /whence list - list the existing waypoints by name.
* /whence new a b c - create waypoint with name \"a b c\" and set current.
* /whence set a b c - set existing waypoint with name \"a b c\" to current.
* /whence delete a b c - delete waypoint with name \"a b c\".

And that is all. Thank you for your support.

