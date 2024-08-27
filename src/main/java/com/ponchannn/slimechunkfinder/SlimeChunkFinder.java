package com.ponchannn.slimechunkfinder;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class SlimeChunkFinder extends JavaPlugin {
    private Map<Player, Chunk> playerChunkMap = new HashMap<>();
    private Executors executors = new Executors(playerChunkMap);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("slimechunkchecker").setExecutor(executors);
        getCommand("slimechunkchecker").setTabCompleter(executors);

        getServer().getPluginManager().registerEvents(new Listeners(playerChunkMap), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}