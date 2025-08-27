package net.mat0u5.pastlife.lives;

import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LivesManager extends ConfigManager {

    private Map<UUID, Integer> livesMap = new HashMap<>();

    public LivesManager() {
        super(".", "_pastlife_lives.txt");
    }

    private void saveLives(ServerPlayerEntity player, int lives) {
        setProperty(player.getEntityName(), String.valueOf(lives));
        livesMap.put(player.getUuid(), lives);
    }

    private int loadLives(ServerPlayerEntity player) {
        int lives = getOrCreateInt(player.getEntityName(), 6);
        livesMap.put(player.getUuid(), lives);
        return lives;
    }

    public int getLives(ServerPlayerEntity player) {
        if (livesMap.containsKey(player.getUuid())) {
            return livesMap.get(player.getUuid());
        }
        return loadLives(player);
    }

    public void setLives(ServerPlayerEntity player, int lives) {
        if (lives < 0) {
            lives = 0;
        }
        saveLives(player, lives);
        if (lives == 0) {
            PlayerUtils.broadcast("§8"+player.getEntityName()+"§f ran out of lives.");
            player.changeGameMode(GameMode.SPECTATOR);
        }
        scoreboardUpdate(player);
    }

    public void addLives(ServerPlayerEntity player, int lives) {
        int currentLives = loadLives(player);
        currentLives += lives;
        setLives(player, currentLives);
    }

    public void scoreboardUpdate(ServerPlayerEntity player) {
        int lives = getLives(player);
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerScoreboard scoreboard = server.getScoreboard();
        scoreboard.getPlayerScore(player.getEntityName(), scoreboard.getObjective("Lives")).setScore(lives);

        if (lives == 0 && !PlayerUtils.isAdmin(player)) {
            player.changeGameMode(GameMode.SPECTATOR);
        }

        teamUpdate(player, lives, scoreboard);
    }

    public void teamUpdate(ServerPlayerEntity player, int lives, Scoreboard scoreboard) {
        String teamName = "Dead";
        if (lives == 1) teamName = "Red";
        if (lives == 2) teamName = "Yellow";
        if (lives == 3) teamName = "Green";
        if (lives >= 4) teamName = "DarkGreen";
        scoreboard.addPlayerToTeam(player.getEntityName(), scoreboard.getTeam(teamName));
    }
}
