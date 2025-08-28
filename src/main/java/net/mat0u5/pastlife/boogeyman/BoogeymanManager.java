package net.mat0u5.pastlife.boogeyman;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BoogeymanManager {
    public static List<UUID> boogeymen = new ArrayList<>();

    public static void rollBoogeymen(MinecraftServer server) {
        PlayerUtils.broadcast("§4The Boogeymen are about to be chosen");
        TaskScheduler.scheduleTask(100, () -> {
            PlayerUtils.playSoundToAllPlayers(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO.value(), 1, 1);
            PlayerUtils.sendTitleToAllPlayers("§a3", 10, 15, 10);
        });
        TaskScheduler.scheduleTask(130, () -> {
            PlayerUtils.playSoundToAllPlayers(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO.value(), 1, 1);
            PlayerUtils.sendTitleToAllPlayers("§e2", 10, 15, 10);
        });
        TaskScheduler.scheduleTask(160, () -> {
            PlayerUtils.playSoundToAllPlayers(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO.value(), 1, 1);
            PlayerUtils.sendTitleToAllPlayers("§c1", 10, 15, 10);
        });
        TaskScheduler.scheduleTask(190, () -> {
            PlayerUtils.playSoundToAllPlayers(SoundEvent.of(Identifier.of("pastlife_boogeyman_wait")), 1, 1);
            PlayerUtils.sendTitleToAllPlayers("§6You are...", 10, 50, 20);
        });
        TaskScheduler.scheduleTask(280, () -> boogeymenChooseRandom(server));
    }

    public static void boogeymenChooseRandom(MinecraftServer server) {
        List<ServerPlayerEntity> players = PlayerUtils.getAllPlayers();
        if (players.isEmpty()) {
            return;
        }
        boogeymen.clear();
        Collections.shuffle(players);

        int chooseBoogeymen = 1;
        while (0.5 >= Math.random()) {
            chooseBoogeymen++;
        }
        Main.log("Choosing "+chooseBoogeymen+ " boogeymen.");

        List<ServerPlayerEntity> boogeymenList = new ArrayList<>();
        List<ServerPlayerEntity> normalList = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            if (chooseBoogeymen > 0 && Main.livesManager.getLives(player) > 1) {
                boogeymenList.add(player);
                boogeymen.add(player.getUuid());
                Main.log(player.getNameForScoreboard() + " has been chosen as a Boogeyman!");
                chooseBoogeymen--;
            }
            else {
                normalList.add(player);
            }
        }

        PlayerUtils.playSoundToPlayers(normalList, SoundEvent.of(Identifier.of("pastlife_boogeyman_no")), 1, 1);
        PlayerUtils.sendTitleToPlayers(normalList, "§aNOT the Boogeyman.", 10, 50, 20);

        PlayerUtils.playSoundToPlayers(boogeymenList, SoundEvent.of(Identifier.of("pastlife_boogeyman_yes")), 1, 1);
        PlayerUtils.sendTitleToPlayers(boogeymenList, "§cThe Boogeyman.", 10, 50, 20);

        TaskScheduler.scheduleTask(100, () -> {
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7You are the boogeyman.");
        });
        TaskScheduler.scheduleTask(140, () -> {
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7You must by any means necessary kill a §agreen§7 or §eyellow§7 name");
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7by direct action to be cured of the curse.");
        });
        TaskScheduler.scheduleTask(260, () -> {
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7If you succeed, run \"/boogeyman success\", you will be cured.");
        });
        TaskScheduler.scheduleTask(320, () -> {
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7If you fail, run \"/boogeyman fail\", and you will");
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7become a §cred name§7.");
        });
        TaskScheduler.scheduleTask(380, () -> {
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7Other players may defend themselves.");
        });
        TaskScheduler.scheduleTask(440, () -> {
            PlayerUtils.broadcastToPlayers(boogeymenList, "§7Voluntary sacrifices will not cure the curse.");
        });
    }
}
