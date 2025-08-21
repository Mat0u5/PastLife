package net.mat0u5.pastlife.lives;

import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.entity.living.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LivesManager extends ConfigManager {

    private Map<UUID, Integer> livesMap = new HashMap<>();

    public LivesManager() {
        super(".", "_pastlife_lives.txt");
    }

    private void saveLives(PlayerEntity player, int lives) {
        setProperty(player.getName(), String.valueOf(lives));
        livesMap.put(player.getUuid(), lives);
    }

    private int loadLives(PlayerEntity player) {
        int lives = getOrCreateInt(player.getName(), 6);
        livesMap.put(player.getUuid(), lives);
        return lives;
    }

    public int getLives(PlayerEntity player) {
        if (livesMap.containsKey(player.getUuid())) {
            return livesMap.get(player.getUuid());
        }
        return loadLives(player);
    }

    public void setLives(PlayerEntity player, int lives) {
        if (lives < 0) {
            lives = 0;
        }
        saveLives(player, lives);
        if (lives == 0) {
            PlayerUtils.broadcast("§8"+player.getName()+"§f ran out of lives.");
        }
    }

    public void addLives(PlayerEntity player, int lives) {
        int currentLives = loadLives(player);
        currentLives += lives;
        setLives(player, currentLives);
    }

    public String getColorCode(PlayerEntity player) {
        Integer lives = getLives(player);
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
