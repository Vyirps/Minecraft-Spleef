package me.jdcomputers.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryDefaults {
    public static void setupCreation(Player player) {
        final ItemStack white = new ItemStack(Material.WHITE_CONCRETE);
        final ItemStack black = new ItemStack(Material.BLACK_CONCRETE);
        final ItemStack red = new ItemStack(Material.RED_CONCRETE);
        final ItemStack orange = new ItemStack(Material.ORANGE_CONCRETE);
        final ItemStack yellow = new ItemStack(Material.YELLOW_CONCRETE);
        final ItemStack green = new ItemStack(Material.GREEN_CONCRETE);
        final ItemStack blue = new ItemStack(Material.BLUE_CONCRETE);
        final ItemStack purple = new ItemStack(Material.PURPLE_CONCRETE);
        final ItemStack pink = new ItemStack(Material.PINK_CONCRETE);

        player.getInventory().setItem(0, white);
        player.getInventory().setItem(1, black);
        player.getInventory().setItem(2, red);
        player.getInventory().setItem(3, orange);
        player.getInventory().setItem(4, yellow);
        player.getInventory().setItem(5, green);
        player.getInventory().setItem(6, blue);
        player.getInventory().setItem(7, purple);
        player.getInventory().setItem(8, pink);
    }
}
