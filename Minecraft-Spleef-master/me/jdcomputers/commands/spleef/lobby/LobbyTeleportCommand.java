package me.jdcomputers.commands.spleef.lobby;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyTeleportCommand extends Command {
    public LobbyTeleportCommand(Command parent) {
        super("Teleport", "Teleport to the lobby to edit it.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.LOBBY_TELEPORT, Permissions.LOBBY_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        FileManager config = Spleef.getInstance().getSpleefConfig().load();

        if (!config.has("lobby")) {
            player.sendMessage(ChatColor.RED + "The lobby location has not been set yet. Use " + ChatColor.WHITE + "/spleef lobby set" + ChatColor.RED + " to set the lobby location.");

            return;
        }

        player.sendMessage(ChatColor.GOLD + "Teleporting...");
        player.teleport(config.getLocation("lobby"));
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
