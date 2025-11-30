package me.jdcomputers.commands.spleef.arena;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ArenaListCommand extends Command {
    public ArenaListCommand(Command parent) {
        super("List", "List all current arenas.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_LIST, Permissions.ARENA_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        final int ITEMS_PER_PAGE = 10;

        int page = 1;

        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);

                if (page < 1)
                    page = 1;
            } catch (NumberFormatException ignored) {
            }
        }

        FileManager arena = Spleef.getInstance().getArenas().load();

        Set<String> keys = arena.getSection("arenas").getKeys(false);
        String[] ordered = keys.stream().sorted().toArray(String[]::new);

        player.sendMessage(ChatColor.GREEN + "-=-=-=-=- SPLEEF ARENAS -=-=-=-=-");

        int start = Math.max((page - 1) * ITEMS_PER_PAGE, 0);
        int end = Math.min(page * ITEMS_PER_PAGE, ordered.length);

        for (int i = start; i < end; i++)
            player.sendMessage(ChatColor.GOLD + ordered[i]);

        if (end < ordered.length)
            player.sendMessage(ChatColor.RED + "Use: /spleef arena list <page_number> for different pages.");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
