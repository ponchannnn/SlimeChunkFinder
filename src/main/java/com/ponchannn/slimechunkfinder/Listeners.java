package com.ponchannn.slimechunkfinder;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class Listeners implements Listener {
    private Map<Player, Chunk> playerChunkMap;

    public Listeners (Map<Player, Chunk> playerChunkMap) {
        this.playerChunkMap = playerChunkMap;
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event) {
        if (playerChunkMap.isEmpty()) return;
        Player player = event.getPlayer();
        if (playerChunkMap.containsKey(player)) {
            Chunk currentChunk = event.getTo().getChunk();
            Chunk previousChunk = playerChunkMap.get(player);

            if (previousChunk == null || !currentChunk.equals(previousChunk)) {
                playerChunkMap.put(player, currentChunk);
                checkSlimeChunk(player, currentChunk);
            }
        }
    }

    private void checkSlimeChunk(Player player, Chunk chunk) {
        if (chunk.isSlimeChunk()) {
            player.sendMessage("You are in a slime chunk!");
        } else {
            player.sendMessage("This is not a slime chunk.");
        }
    }
}
