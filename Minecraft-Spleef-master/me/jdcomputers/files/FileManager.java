package me.jdcomputers.files;

import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public final class FileManager {
    private final Spleef spleef;
    private final String path;
    private final String filename;
    private final File file;

    private YamlConfiguration config;

    public FileManager(Spleef spleef, String path, String filename) {
        this.spleef = spleef;
        this.path = spleef.getDataFolder() + File.separator + path;
        this.filename = filename;

        file = new File(this.path, this.filename);
    }

    public void set(Object key, Object value) {
        config.set(key.toString(), value);
    }

    public boolean has(Object key) {
        return get(key) != null;
    }

    public Object get(Object key) {
        return config.get(key.toString());
    }

    public int getInt(Object key) {
        return config.getInt(key.toString());
    }

    public long getLong(Object key) {
        return config.getLong(key.toString());
    }

    public double getDouble(Object key) {
        return config.getDouble(key.toString());
    }

    public boolean getBoolean(Object key) {
        return config.getBoolean(key.toString());
    }

    public String getString(Object key) {
        return config.getString(key.toString());
    }

    public OfflinePlayer getPlayer(Object key) {
        return config.getOfflinePlayer(key.toString());
    }

    public ItemStack getItemStack(Object key) {
        return config.getItemStack(key.toString());
    }

    public Location getLocation(Object key) {
        return config.getLocation(key.toString());
    }

    public Color getColor(Object key) {
        return config.getColor(key.toString());
    }

    public ConfigurationSection getSection(Object key) {
        return config.getConfigurationSection(key.toString());
    }

    public List<?> getList(Object key) {
        return config.getList(key.toString());
    }

    public Set<String> getKey(boolean deep) {
        return config.getKeys(deep);
    }

    public FileManager load() {
        config = YamlConfiguration.loadConfiguration(file);

        return this;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            spleef.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Failed to save to file: " + e.getMessage());
        }
    }

    public File getFile() {
        return file;
    }

    public Spleef getSpleef() {
        return spleef;
    }

    public String getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
