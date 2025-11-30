package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpleefConfigCommand extends Command {
    public SpleefConfigCommand(Command parent) {
        super("Config", "Modify the configuration file for spleef.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.CONFIG)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Correct usage: /spleef config <key>");

            return;
        }

        FileManager config = Spleef.getInstance().getSpleefConfig().load();

        if (args[1].equalsIgnoreCase("digger")) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "You can't save the default pickaxe as air.");

                return;
            }

            config.set("digger", item);
            config.save();

            player.sendMessage(ChatColor.GOLD + "Successfully changed the digger to " + item.getType().name().toLowerCase() + ".");

            return;
        }

        if (args[1].equalsIgnoreCase("drop_concrete_likelihood")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Correct usage: /spleef config drop_concrete_likelihood <value>");

                return;
            }

            try {
                int percent = Integer.parseInt(args[2]);

                if (percent < 0 || percent > 100) {
                    player.sendMessage(ChatColor.RED + "The number must a whole number be between 0-100");

                    return;
                }

                config.set("drop_concrete_likelihood", percent);
                config.save();

                player.sendMessage(ChatColor.GOLD + "Successfully saved the concrete drop rate to " + percent + "%.");
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "The number must a whole number be between 0-100");
            }

            return;
        }

        player.sendMessage(ChatColor.RED + "That was not a valid key.");
        player.sendMessage(ChatColor.RED + "Correct usage: /spleef config <key>");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.CONFIG))
            return null;

        List<String> output = new ArrayList<>();

        output.add("digger");
        output.add("drop_concrete_likelihood");

        return output;
    }
}
