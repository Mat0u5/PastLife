package net.mat0u5.pastlife.secretsociety;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.*;

public class SecretSociety {
    public static List<UUID> members = new ArrayList<>();
    public static List<UUID> yetToInitiate = new ArrayList<>();
    public static boolean active = false;
    public static boolean began = false;
    public static boolean ended = false;
    public static long ticks = 0;
    public static String secretWord = "Duck";
    public static final int MEMBERS = 3;
    public static final int INITIATE_MESSAGE_DELAYS = 15*20;
    public static final String[] randomWords = new String[]{"Hammer","Magnet","Throne","Gravity","Puzzle","Spiral","Pivot","Flare"};
    public static Random rnd = new Random();

    public static void tick(MinecraftServer server) {
        if (!active) return;
        if (yetToInitiate.isEmpty()) return;
        ticks++;
        if (ticks < 250) return;
        if (ticks % INITIATE_MESSAGE_DELAYS == 0) {
            for (ServerPlayerEntity player : PlayerUtils.getAllPlayers()) {
                if (yetToInitiate.contains(player.getUuid())) {
                    player.sendMessage(new LiteralText("§7When you are alone, type \"/initiate\""));
                }
            }
        }
    }

    public static void beginSociety(MinecraftServer server) {
        String randomWord = randomWords[rnd.nextInt(randomWords.length)];
        beginSociety(server, randomWord);
    }

    public static void beginSociety(MinecraftServer server, String word) {
        secretWord = word;
        active = true;
        began = true;
        ended = false;
        ticks = 0;

        members.clear();
        yetToInitiate.clear();
        List<ServerPlayerEntity> players = PlayerUtils.getAllPlayers();
        Collections.shuffle(players);

        List<ServerPlayerEntity> memberPlayers = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            if (player == null) continue;
            if (Main.livesManager != null && Main.livesManager.getLives(player) <= 0) continue;

            if (members.size() < MEMBERS) {
                memberPlayers.add(player);
                members.add(player.getUuid());
                Main.log(player.getName()+" has been chosen as a member of the Secret Society.");
            }
        }
        yetToInitiate = new ArrayList<>(members);

        PlayerUtils.broadcastToPlayers(memberPlayers, "§7Do not read out loud. Only you will see the way forward.");


        TaskScheduler.scheduleTask(30, () -> {
            PlayerUtils.playSoundToAllPlayers(new SoundEvent(new Identifier("pastlife_secretsociety_whisper")), 1, 1);
            PlayerUtils.sendTitleToPlayers(memberPlayers, "§cThe Society calls", 0, 30, 0);
        });
        TaskScheduler.scheduleTask(45, () -> {
            PlayerUtils.sendTitleToPlayers(memberPlayers, "§cThe Society calls.", 0, 30, 0);
        });
        TaskScheduler.scheduleTask(60, () -> {
            PlayerUtils.sendTitleToPlayers(memberPlayers, "§cThe Society calls..", 0, 30, 0);
        });
        TaskScheduler.scheduleTask(75, () -> {
            PlayerUtils.sendTitleToPlayers(memberPlayers, "§cThe Society calls...", 0, 45, 30);
        });
        TaskScheduler.scheduleTask(145, () -> {
            PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§cTake yourself somewhere quiet", 20, 60, 20);
        });
    }

    public static void initiatePlayer(ServerPlayerEntity player) {
        if (!members.contains(player.getUuid())) return;
        if (!yetToInitiate.contains(player.getUuid())) return;
        yetToInitiate.remove(player.getUuid());

        PlayerUtils.playSoundToPlayer(player, new SoundEvent(new Identifier("pastlife_secretsociety_whisper")), 1, 1);

        int currentTime = 20;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7You have been chosen to be part of the §csecret society§7."));
        });
        currentTime += 50;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7There are §c2§7 other members. Find them."));
        });
        currentTime += 80;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7Together, secretly kill §c2§7 other players by §cnon-pvp§7 means."));
        });
        currentTime += 100;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7Find the other members with the secret word:"));
        });
        currentTime += 80;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§d\""+secretWord+"\""));
        });
        currentTime += 80;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7Type \"/society success\" when you complete your goal."));
        });
        currentTime += 80;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7Don't tell anyone else about the society."));
        });
        currentTime += 70;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7If you fail..."));
        });
        currentTime += 70;
        TaskScheduler.scheduleTask(currentTime, () -> {
            player.sendMessage(new LiteralText("§7Type \"/society fail\", and you all lose §c2 lives§7."));
        });
    }

    public static void end(MinecraftServer server, boolean success) {
        if (ended) return;
        active = false;
        ended = true;
        ticks = 0;
        PlayerUtils.playSoundToAllPlayers(new SoundEvent(new Identifier("pastlife_secretsociety_whisper")), 1, 1);

        List<ServerPlayerEntity> players = PlayerUtils.getAllPlayers();
        List<ServerPlayerEntity> memberPlayers = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            if (player == null) continue;
            if (members.contains(player.getUuid())) {
                memberPlayers.add(player);
            }
        }

        if (success) {
            PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§aThe Society is pleased", 20, 30, 20);
            TaskScheduler.scheduleTask(75, () -> {
                PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§aYou will not be punished", 20, 30, 20);
            });
            TaskScheduler.scheduleTask(150, () -> {
                PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§cYou are still sworn to secrecy", 20, 30, 20);
            });
        }
        else {
            PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§cThe Society is displeased", 20, 30, 20);
            TaskScheduler.scheduleTask(75, () -> {
                PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§cYou will be punished", 20, 30, 20);
            });
            TaskScheduler.scheduleTask(110, () -> {
                for (ServerPlayerEntity member : memberPlayers) {
                    if (member.getHealth() <= 1) {
                        member.setHealth(2);
                    }
                    member.damage(DamageSource.player(member), 1);
                    if (Main.livesManager != null) {
                        Main.livesManager.addLives(member, -2);
                    }
                }
            });
            TaskScheduler.scheduleTask(150, () -> {
                PlayerUtils.sendSubtitleToPlayers(memberPlayers, "§cYou are still sworn to secrecy", 20, 30, 20);
            });
        }
    }
}
