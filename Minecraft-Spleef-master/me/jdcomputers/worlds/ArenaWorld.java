package me.jdcomputers.worlds;

import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldType;

public class ArenaWorld extends CustomWorld {
    private World world;

    public ArenaWorld() {
        super("spleef_arenas");
    }

    @Override
    public void build() {
        creator.generatorSettings("{\"layers\": [{\"block\": \"air\", \"height\": 1}], \"biome\":\"plains\"}");

        world = creator.createWorld();

        assert world != null;

        world.setDifficulty(Difficulty.EASY);
        world.setStorm(false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.FALL_DAMAGE, false);
        world.setTime(12000L);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
