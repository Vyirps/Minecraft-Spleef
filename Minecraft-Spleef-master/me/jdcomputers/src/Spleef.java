package me.jdcomputers.src;

import me.jdcomputers.commands.CommandCollection;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.worlds.ArenaWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Spleef extends JavaPlugin {
    private static Spleef SINGLETON;

    private final ArenaWorld arenaWorld = new ArenaWorld();
    private final FileManager config = new FileManager(this, "", "config.yml").load();
    private final FileManager arenas = new FileManager(this, "", "arenas.yml").load();
    private final SpleefGame game = new SpleefGame(this);

    public static Spleef getInstance() {
        return SINGLETON;
    }

    @Override
    public void onEnable() {
        SINGLETON = this;

        this.getLogger().info("Creating arena world for spleef...");

        arenaWorld.build();

        this.getLogger().info("Arena world successfully created.");
        this.getLogger().info("Enabled");
        this.getServer().getPluginManager().registerEvents(new MyListener(this), this);

        setDefaults();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabled");

        game.clearPlayers();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (me.jdcomputers.commands.Command cmd : CommandCollection.COMMANDS) {
            if (!label.equalsIgnoreCase(cmd.getName()))
                continue;

            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be run by in-game players.");

                return false;
            }

            if (args.length == 0 || cmd.getSubCommands().isEmpty()) {
                cmd.run(player, args);

                return true;
            }

            for (String arg : args) {
                me.jdcomputers.commands.Command previous = cmd;

                cmd = cmd.find(arg);

                if (cmd == null) {
                    previous.run(player, args);

                    return true;
                }
            }

            cmd.run(player, args);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (me.jdcomputers.commands.Command cmd : CommandCollection.COMMANDS) {
            if (!label.equalsIgnoreCase(cmd.getName()))
                continue;

            if (!(sender instanceof Player player))
                return null;

            if (args.length == 0 || cmd.getSubCommands().isEmpty())
                return cmd.tabComplete(player, args);

            for (String arg : args) {
                me.jdcomputers.commands.Command previous = cmd;

                cmd = cmd.find(arg);

                if (cmd == null)
                    return previous.tabComplete(player, args);
            }

            return cmd.tabComplete(player, args);
        }

        return null;
    }

    private void setDefaults() {
        boolean changeOccurred = false;

        if (!config.has("digger")) {
            ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE, 1);

            item.addUnsafeEnchantment(Enchantment.EFFICIENCY, 20);
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 20);

            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                List<String> lore = new ArrayList<>();

                lore.add(ChatColor.LIGHT_PURPLE + "The epic pickaxe weilded by the fierce player.");
                lore.add(ChatColor.LIGHT_PURPLE + "The player will CREAM all other players");
                lore.add(ChatColor.LIGHT_PURPLE + "with this pickaxe by digging the ground");
                lore.add(ChatColor.LIGHT_PURPLE + "out below them!");

                assert meta != null;
                meta.setDisplayName(ChatColor.AQUA + "Spleef Pickaxe");
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                item.setItemMeta(meta);
            }

            config.set("digger", item);

            changeOccurred = true;
        }

        if (!config.has("drop_concrete_likelihood")) {
            config.set("drop_concrete_likelihood", 50);

            changeOccurred = true;
        }

        if (changeOccurred)
            config.save();
    }

    public FileManager getSpleefConfig() {
        return config;
    }

    public FileManager getArenas() {
        return arenas;
    }

    public ArenaWorld getArenaWorld() {
        return arenaWorld;
    }

    public SpleefGame getGame() {
        return game;
    }
}
