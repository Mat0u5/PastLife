package net.mat0u5.pastlife.client.lives;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.LivesUpdatePacket;
import net.minecraft.entity.living.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class ClientLivesManager {
    private static Map<String, Integer> livesMap = new HashMap<>();
    public static void handlePacket(LivesUpdatePacket packet) {
        String playerName = packet.playerName;
        int lives = packet.lives;
        if (playerName == null || playerName.isEmpty()) {
            Main.error("Invalid player name received in LivesUpdatePacket.");
            return;
        }
        livesMap.put(playerName, lives);
    }

    public static Integer getPlayerLives(PlayerEntity player) {
        return getPlayerLives(player.name);
    }

    public static Integer getPlayerLives(String playerName) {
        if (livesMap.containsKey(playerName)) {
            return livesMap.get(playerName);
        }
        return null;
    }

    public static String getColorCode(PlayerEntity player) {
        return getColorCode(player.name);
    }

    public static String getColorCode(String playerName) {
        Integer lives = getPlayerLives(playerName);
        if (lives == null) {
            return null;
        }
        if (lives <= 0) return "§8"; // Dark gray
        if (lives == 1) return "§c"; // Red
        if (lives == 2) return "§e"; // Yellow
        if (lives == 3) return "§a"; // Green
        return "§2"; // Dark green
    }
}
