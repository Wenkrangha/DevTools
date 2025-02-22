package com.wenkrang.devTools;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class DevTools extends JavaPlugin {
    public static ItemStack itemStack;
    public static Inventory inventory;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("dv").setExecutor(new Command());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
