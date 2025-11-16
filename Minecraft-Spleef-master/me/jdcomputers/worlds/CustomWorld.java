package me.jdcomputers.worlds;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public abstract class CustomWorld {
    protected final WorldCreator creator;
    protected final String name;

    public CustomWorld(String name) {
        this.name = name;
        this.creator = new WorldCreator(name);

        creator.generateStructures(false);
        creator.environment(World.Environment.NORMAL);
        creator.type(WorldType.FLAT);
    }

    public abstract void build();
    public abstract World getWorld();

    public String getName() {
        return name;
    }

    public WorldCreator getCreator() {
        return creator;
    }
}
