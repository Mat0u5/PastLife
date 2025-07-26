package net.mat0u5.pastlife.lives;

import net.minecraft.entity.living.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class LivesManager extends ConfigManager {

    private Map<String, Integer> livesMap = new HashMap<>();

    public LivesManager() {
        super(".", "_pastlife_lives.txt");
    }

    private void saveLives(PlayerEntity player, int lives) {
        setProperty(player.name, String.valueOf(lives));
        livesMap.put(player.name, lives);
    }

    private int loadLives(PlayerEntity player) {
        int lives = getOrCreateInt(player.name, 6);
        livesMap.put(player.name, lives);
        return lives;
    }

    public int getLives(PlayerEntity player) {
        if (livesMap.containsKey(player.name)) {
            return livesMap.get(player.name);
        }
        return loadLives(player);
    }

    public void setLives(PlayerEntity player, int lives) {
        if (lives < 0) {
            lives = 0;
        }
        saveLives(player, lives);
    }

    public void addLives(PlayerEntity player, int lives) {
        int currentLives = loadLives(player);
        currentLives += lives;
        saveLives(player, currentLives);
    }
}
