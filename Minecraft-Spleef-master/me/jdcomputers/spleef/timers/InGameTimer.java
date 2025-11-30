package me.jdcomputers.spleef.timers;

import me.jdcomputers.events.SpleefGameCancelledEvent;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.worldedit.WorldEditCreations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InGameTimer extends GameTimer {
    private FileManager arena;
    private int times;

//    public InGameTimer(SpleefGame game, long delay) {
//        super(game, 3 0, 5, delay, 20L);
//
//        arena = game.getPlugin().getArenas().load();
//    }

    public InGameTimer(SpleefGame game, long delay) {
        super(game, 30, 5, delay, 20L, false);
        
        arena = game.getPlugin().getArenas().load();
    }

    
    
    
    @Override
    protected void timerTick() {
        if (game.getPlayingPlayers().size() < 2) {//
            SpleefGameCancelledEvent event = new SpleefGameCancelledEvent(game);

            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    protected void timerInitialized() {
        maximum = Math.min(game.getLivingPlayers().size() * 5, 30);

        times++;

        if (times > 10 && game.getLivingPlayers().size() == 2)
            maximum /= 2;

        arena = game.getPlugin().getArenas().load();

        for (SpleefPlayer player : game.getPlayingPlayers())
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 1, true, false, false));
    }


    
    private int currLayer = 150;
    private int gapMinus = 9;
    
    

    @Override
    protected boolean timerPast() {
        Location loc = WorldEditCreations.ARENA_LOCATION.clone();

        int minX = loc.getBlockX();
        int minY = loc.getBlockY();
        int minZ = loc.getBlockZ();
        int maxX = minX + 100;
        int maxY = minY + 100;
        int maxZ = minZ + 100;

        Bukkit.getLogger().info("Arena bounds - minX: " + minX + ", minY: " + minY + ", minZ: " + minZ + 
                ", maxX: " + maxX + ", maxY: " + maxY + ", maxZ: " + maxZ);



        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = maxY; y >= minY; y--) {
                    Location locCurrent = new Location(loc.getWorld(), x, y, z);
                    Block currentBlock = locCurrent.getBlock();

                    if (currentBlock.getType() != Material.AIR) {
                    	if(y >=  currLayer) { //stopper, checks if its checking for top most 
                            currentBlock.setType(Material.AIR); // Delete this top block
                    	}
                        break; // Move on to next column
                    }
                }
            }
        }
        
        currLayer -= gapMinus;

        return true;


    }


    @Override
    protected void timerIncrement(int second) {
        for (SpleefPlayer player : game.getPlayers())
            player.sendMessage(ChatColor.GOLD + "The topmost block layer will be removed in " + ChatColor.GREEN + second + ChatColor.GOLD + " seconds.");
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer player : game.getPlayers())
            player.sendMessage(ChatColor.RED + "The topmost block layer will be removed in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
    }

}
