package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import me.jdcomputers.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefCommand extends Command {
    public SpleefCommand() {
        super("Spleef", "The main command for spleef.");

        addSubCommand(new SpleefHelpCommand(this));
        addSubCommand(new SpleefArenaCommand(this));
        addSubCommand(new SpleefLobbyCommand(this));
        addSubCommand(new SpleefConfigCommand(this));
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, null, Permissions.SPLEEF)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        player.sendMessage(ChatColor.RED + "Please run " + ChatColor.GOLD + "/spleef help" + ChatColor.RED + " for a list of commands.");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, null, Permissions.SPLEEF))
            return null;

        List<String> output = new ArrayList<>();

        output.add("help");
        output.add("arena");
        output.add("lobby");
        output.add("config");

        return output;
    }
}
