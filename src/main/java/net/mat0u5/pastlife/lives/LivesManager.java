package net.mat0u5.pastlife.lives;

import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.world.WorldSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LivesManager extends ConfigManager {

    private Map<UUID, Integer> livesMap = new HashMap<>();

    public LivesManager() {
        super(".", "_pastlife_lives.txt");
    }

    private void saveLives(ServerPlayerEntity player, int lives) {
        setProperty(player.getName(), String.valueOf(lives));
        livesMap.put(player.getUuid(), lives);
    }

    private int loadLives(ServerPlayerEntity player) {
        int lives = getOrCreateInt(player.getName(), 6);
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
            PlayerUtils.broadcast("§8"+player.getName()+"§f ran out of lives.");
            player.setGameMode(WorldSettings.GameMode.SPECTATOR);
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

        Scoreboard scoreboard = server.getWorld(0).getScoreboard();
        scoreboard.getScore(player.getName(), scoreboard.getObjective("Lives")).set(lives);

        if (lives == 0 && !server.getPlayerManager().isOp(player.getGameProfile())) {
            player.setGameMode(WorldSettings.GameMode.SPECTATOR);
        }

        teamUpdate(player, lives, scoreboard);
    }

    public void teamUpdate(ServerPlayerEntity player, int lives, Scoreboard scoreboard) {
        String teamName = "Dead";
        if (lives == 1) teamName = "Red";
        if (lives == 2) teamName = "Yellow";
        if (lives == 3) teamName = "Green";
        if (lives >= 4) teamName = "DarkGreen";
        scoreboard.addMemberToTeam(player.getName(), teamName);
    }
}
