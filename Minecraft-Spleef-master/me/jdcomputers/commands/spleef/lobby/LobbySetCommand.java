package me.jdcomputers.commands.spleef.lobby;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class LobbySetCommand extends Command {
    public LobbySetCommand(Command parent) {
        super("Set", "Set the lobby location for spleef.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.LOBBY_SET, Permissions.LOBBY_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        FileManager config = Spleef.getInstance().getSpleefConfig().load();

        Location loc = player.getLocation();

        config.set("lobby", loc);
        config.save();

        player.sendMessage(ChatColor.GOLD + "The lobby location for spleef was set in " +
                ChatColor.WHITE + Objects.requireNonNull(loc.getWorld()).getName() +
                ChatColor.GOLD + " @ " +
                "X: " + ChatColor.GREEN + loc.getBlockX() + ChatColor.GOLD + ", " +
                "Y: " + ChatColor.GREEN + loc.getBlockY() + ChatColor.GOLD + ", " +
                "Z: " + ChatColor.GREEN + loc.getBlockZ() + ChatColor.GOLD + "."
        );
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
