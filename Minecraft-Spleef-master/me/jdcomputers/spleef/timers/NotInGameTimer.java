package me.jdcomputers.spleef.timers;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class NotInGameTimer extends GameTimer {
    public NotInGameTimer(SpleefGame game, long delay) {
        super(game, 15, 5, delay, 20L, true);
    }

    @Override
    protected void timerTick() {
        if (game.getPlayingPlayers().size() < 1)
            current = 0;
    }

    @Override
    protected void timerInitialized() {
        FileManager config = game.getPlugin().getSpleefConfig().load();

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            if (config.has("lobby"))
                p.getPlayer().teleport(config.getLocation("lobby"));

            p.setup();
        }

        World world = game.getPlugin().getArenaWorld().getWorld();

        if (world == null)
            return;

        List<Entity> items = world.getEntities();

        for (int i = items.size() - 1; i >= 0; i--)
            if (items.get(i) instanceof Item)
                items.get(i).remove();
    }

    @Override
    protected boolean timerPast() {
        FileManager arena = game.getPlugin().getArenas().load();

        if (arena.getSection("arenas").getKeys(false).isEmpty()) {
            for (SpleefPlayer p : game.getPlayingPlayers())
                p.sendMessage(ChatColor.RED + "The game failed to begin since there are no arenas created yet.");

            current = 0;

            return false;
        }

        FileManager config = game.getPlugin().getSpleefConfig().load();

        ItemStack pickaxe = config.getItemStack("digger");

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.setup();
            p.getPlayer().getInventory().setItem(0, pickaxe);

            p.sendMessage(ChatColor.GOLD + "Creating arena; please be patient...");
        }

        return true;
    }

    @Override
    protected void timerIncrement(int second) {
        if (current == 0)
            return;

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GREEN + "The game will begin in " + ChatColor.WHITE + second + " seconds.");
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            float pitch = second / 6.0f + 0.5f;

            p.sendMessage(ChatColor.RED + "The game will begin in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, pitch);
        }
    }
}
