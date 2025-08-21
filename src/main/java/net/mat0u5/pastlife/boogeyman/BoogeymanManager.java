package net.mat0u5.pastlife.boogeyman;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.lives.LivesManager;
import net.mat0u5.pastlife.packets.TitlePacket;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoogeymanManager {
    public static List<String> boogeymen = new ArrayList<>();

    public static void rollBoogeymen(MinecraftServer server) {
        PlayerUtils.sendPacketToAllPlayers(new ChatMessagePacket("§4The Boogeymen are about to be chosen"));
        TaskScheduler.scheduleTask(100, () -> {
            PlayerUtils.playSoundToAllPlayers("didgeridoo", 1, 1);
            PlayerUtils.sendPacketToAllPlayers(new TitlePacket("§a3","", 10, 15, 10));
        });
        TaskScheduler.scheduleTask(130, () -> {
            PlayerUtils.playSoundToAllPlayers("didgeridoo", 1, 1);
            PlayerUtils.sendPacketToAllPlayers(new TitlePacket("§e2","", 10, 15, 10));
        });
        TaskScheduler.scheduleTask(160, () -> {
            PlayerUtils.playSoundToAllPlayers("didgeridoo", 1, 1);
            PlayerUtils.sendPacketToAllPlayers(new TitlePacket("§c1","", 10, 15, 10));
        });
        TaskScheduler.scheduleTask(190, () -> {
            PlayerUtils.playSoundToAllPlayers("boogeyman_wait", 1, 1);
            PlayerUtils.sendPacketToAllPlayers(new TitlePacket("§6You are...","", 10, 50, 20));
        });
        TaskScheduler.scheduleTask(280, () -> boogeymenChooseRandom(server));
    }

    public static void boogeymenChooseRandom(MinecraftServer server) {
        List<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().players);
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
                boogeymen.add(player.name);
                Main.log(player.name + " has been chosen as a Boogeyman!");
                chooseBoogeymen--;
            }
            else {
                normalList.add(player);
            }
        }

        PlayerUtils.playSoundToPlayers(normalList, "boogeyman_no", 1, 1);
        PlayerUtils.sendPacketToPlayers(normalList, new TitlePacket("§aNOT the Boogeyman.","", 10, 50, 20));

        PlayerUtils.playSoundToPlayers(boogeymenList, "boogeyman_yes", 1, 1);
        PlayerUtils.sendPacketToPlayers(boogeymenList, new TitlePacket("§cThe Boogeyman.","", 10, 50, 20));

        TaskScheduler.scheduleTask(100, () -> {
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7You are the boogeyman."));
        });
        TaskScheduler.scheduleTask(140, () -> {
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7You must by any means necessary kill a §agreen§7 or §eyellow§7 name"));
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7by direct action to be cured of the curse."));
        });
        TaskScheduler.scheduleTask(260, () -> {
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7If you succeed, run \"/boogeyman succeed\", you will be cured."));
        });
        TaskScheduler.scheduleTask(320, () -> {
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7If you fail, run \"/boogeyman fail\", and you will"));
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7become a §cred name§7."));
        });
        TaskScheduler.scheduleTask(380, () -> {
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7Other players may defend themselves."));
        });
        TaskScheduler.scheduleTask(440, () -> {
            PlayerUtils.sendPacketToPlayers(boogeymenList, new ChatMessagePacket("§7Voluntary sacrifices will not cure the curse."));
        });
    }
}
