package me.jdcomputers.worldedit;

import me.jdcomputers.src.Spleef;
import org.bukkit.Location;
import org.bukkit.Material;

public final class WorldEditCreations {
    public static final Location ARENA_LOCATION = new Location(Spleef.getInstance().getArenaWorld().getWorld(), -2000, 50, -2000);

    public static void setBlocks(Material material, Location from, Location to) {
        int minX = Math.min(from.getBlockX(), to.getBlockX());
        int minY = Math.min(from.getBlockY(), to.getBlockY());
        int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
        int maxX = Math.max(from.getBlockX(), to.getBlockX());
        int maxY = Math.max(from.getBlockY(), to.getBlockY());
        int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(from.getWorld(), x, y, z);

                    loc.getBlock().setType(material);
                }
            }
        }
    }

    public static void createWall(Material material, Location from, Location to) {
        int minX = Math.min(from.getBlockX(), to.getBlockX());
        int minY = Math.min(from.getBlockY(), to.getBlockY());
        int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
        int maxX = Math.max(from.getBlockX(), to.getBlockX());
        int maxY = Math.max(from.getBlockY(), to.getBlockY());
        int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());

        Location corner1 = new Location(from.getWorld(), minX, minY, minZ);
        Location corner2 = new Location(from.getWorld(), maxX, maxY, minZ);
        Location corner3 = new Location(from.getWorld(), maxX, minY, maxZ);
        Location corner4 = new Location(from.getWorld(), minX, maxY, maxZ);

        setBlocks(material, corner1, corner2);
        setBlocks(material, corner2, corner3);
        setBlocks(material, corner3, corner4);
        setBlocks(material, corner4, corner1);
    }

    public static void clear(Location at, int size, int height) {
        int startX = at.getBlockX();
        int startY = at.getBlockY();
        int startZ = at.getBlockZ();
        int endX = startX + size;
        int endY = startY + height;
        int endZ = startZ + size;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    Location loc = new Location(at.getWorld(), x, y, z);

                    if (loc.getBlock().getType().isAir())
                        continue;

                    loc.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public static void copy(Location from, Location to, Location paste) {
        int minX = Math.min(from.getBlockX(), to.getBlockX());
        int minY = Math.min(from.getBlockY(), to.getBlockY());
        int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
        int maxX = Math.max(from.getBlockX(), to.getBlockX());
        int maxY = Math.max(from.getBlockY(), to.getBlockY());
        int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(from.getWorld(), x, y, z);
                    Location newLoc = paste.clone().add(x - minX, y - minY, z - minZ);

                    if (loc.getBlock().getType().equals(newLoc.getBlock().getType()))
                        continue;

                    newLoc.getBlock().setType(loc.getBlock().getType());
                }
            }
        }
    }
}
