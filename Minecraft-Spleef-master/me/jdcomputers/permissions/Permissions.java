package me.jdcomputers.permissions;

import org.bukkit.entity.Player;

public final class Permissions {
    // Main command
    public static final String SPLEEF = "spleef";

    // Base commands
    public static final String ARENA = "spleef.arena";
    public static final String CONFIG = "spleef.config";
    public static final String HELP = "spleef.help";
    public static final String LOBBY = "spleef.lobby";

    // Arena commands
    public static final String ARENA_CREATE = "spleef.arena.create";
    public static final String ARENA_DELETE = "spleef.arena.delete";
    public static final String ARENA_EDIT = "spleef.arena.edit";
    public static final String ARENA_LIST = "spleef.arena.list";
    public static final String ARENA_SPAWN = "spleef.arena.spawn";

    // Lobby commands
    public static final String LOBBY_SET = "spleef.lobby.set";
    public static final String LOBBY_TELEPORT = "spleef.lobby.teleport";

    // Star commands
    public static final String ALL = "*";
    public static final String SPLEEF_ALL = "spleef.*";
    public static final String ARENA_ALL = "spleef.arena.*";
    public static final String LOBBY_ALL = "spleef.lobby.*";

    public static boolean hasPermission(Player player, String star, String specific, String...otherPermissions) {
        if (player.isOp())
            return true;

        for (String item : otherPermissions)
            if (player.hasPermission(item))
                return true;

        if (star == null || star.trim().isEmpty())
            return player.hasPermission(ALL) || player.hasPermission(specific);

        return player.hasPermission(ALL) || player.hasPermission(star) || player.hasPermission(specific);
    }
}
