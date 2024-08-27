package com.ponchannn.slimechunkfinder;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Executors implements CommandExecutor, TabCompleter {
    private Map<Player, Chunk> playerChunkMap;
    private boolean checkEnabled = false;

    public Executors (Map<Player, Chunk> playerChunkMap) {
        this.playerChunkMap = playerChunkMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length > 1 && args[1].equalsIgnoreCase("true")) {
                        checkEnabled = true;
                        player.sendMessage("Slime chunk checker enabled.");
                        playerChunkMap.put(player, player.getLocation().getChunk());
                    } else {
                        checkEnabled = false;
                        player.sendMessage("Slime chunk checker disabled.");
                        playerChunkMap.remove(player);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("show")) {
                    showSurroundingSlimeChunks(player);
                    return true;
                }
            }
        }
        return false;
    }

    private void showSurroundingSlimeChunks(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        int playerChunkX = loc.getChunk().getX();
        int playerChunkZ = loc.getChunk().getZ();

        float yaw = loc.getYaw(); // プレイヤーの向き
        int[][] directions = getRotatedDirections(yaw);

        StringBuilder message = new StringBuilder("Nearby slime chunks:\n");

        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                int x = playerChunkX + directions[0][0] * i + directions[1][0] * j;
                int z = playerChunkZ + directions[0][1] * i + directions[1][1] * j;

                Chunk chunk = world.getChunkAt(x, z);
                if (chunk.isSlimeChunk()) {
                    if (i == 0 && j == 0) message.append("\uD83D\uDD34 ");
                    else message.append("🟩 ");
                } else {
                    if (i == 0 && j == 0) message.append("❌ ");
                    else message.append("⬛ ");
                }
            }
            message.append("\n");
        }
        player.sendMessage(message.toString());
    }

    private int[][] getRotatedDirections(float yaw) {
        // 向いている方向を基に行列を回転させる
        yaw = (yaw % 360 + 360) % 360; // 正規化 (0 <= yaw < 360)

        if (yaw >= 315 || yaw < 45) { // 南向き (デフォルト)
            return new int[][]{{0, -1}, {-1, 0}};
        } else if (yaw >= 45 && yaw < 135) { // 西向き
            return new int[][]{{1, 0}, {0, -1}};
        } else if (yaw >= 135 && yaw < 225) { // 北向き
            return new int[][]{{0, 1}, {1, 0}};
        } else { // 東向き
            return new int[][]{{-1, 0}, {0, 1}};
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        List<String> COMMANDS = new ArrayList<>();
        if (args.length == 1) {
            COMMANDS.add("set");
            COMMANDS.add("show");

            StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
            Collections.sort(completions);
            return completions;
        } else if (args.length == 2) {
            String action = args[0].toLowerCase();
            if (action.equals("set")) {
                COMMANDS.add("true");
                COMMANDS.add("false");
            }
            StringUtil.copyPartialMatches(args[1], COMMANDS, completions);
            Collections.sort(completions);
            return completions;
        }

        return completions;
    }
}
