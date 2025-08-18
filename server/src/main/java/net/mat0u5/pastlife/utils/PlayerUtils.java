package net.mat0u5.pastlife.utils;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.SoundEventPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.mob.hostile.boss.EnderDragonEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import java.util.List;

public class PlayerUtils {
    public static void playSoundToAllPlayers(String sound, float volume, float pitch) {
        playSoundToAllPlayers(sound, volume, pitch, false);
    }
    public static void playSoundToAllPlayers(String sound, float volume, float pitch, boolean stream) {
        if (Main.server == null) {
            return;
        }
        Main.server.playerManager.sendPacket(new SoundEventPacket(sound, volume, pitch, stream));
    }

    public static void playSoundToPlayer(ServerPlayerEntity player, String sound, float volume, float pitch) {
        playSoundToPlayer(player, sound, volume, pitch, false);
    }
    public static void playSoundToPlayer(ServerPlayerEntity player, String sound, float volume, float pitch, boolean stream) {
        player.networkHandler.sendPacket(new SoundEventPacket(sound, volume, pitch, stream));
    }

    public static void playSoundToPlayers(List<ServerPlayerEntity> player, String sound, float volume, float pitch) {
        playSoundToPlayers(player, sound, volume, pitch, false);
    }
    public static void playSoundToPlayers(List<ServerPlayerEntity> player, String sound, float volume, float pitch, boolean stream) {
        for (ServerPlayerEntity serverPlayerEntity : player) {
            playSoundToPlayer(serverPlayerEntity, sound, volume, pitch, stream);
        }
    }

    public static void sendPacketToAllPlayers(Packet packet) {
        if (Main.server == null) {
            return;
        }
        Main.server.playerManager.sendPacket(packet);
    }

    public static void sendPacketToPlayer(ServerPlayerEntity player, Packet packet) {
        if (player == null || player.networkHandler == null) {
            return;
        }
        player.networkHandler.sendPacket(packet);
    }

    public static void sendPacketToPlayers(List<ServerPlayerEntity> players, Packet packet) {
        for (ServerPlayerEntity player : players) {
            sendPacketToPlayer(player, packet);
        }
    }

    public static int actionTriggers = 0;
    public static void doAction(MinecraftServer server) {
        //PORTAL ROOM AT -1016, 24, 423
        if (actionTriggers >= 0) return;
        actionTriggers++;
        for(int i = 0; i < server.playerManager.players.size(); ++i) {
            ServerPlayerEntity player = (ServerPlayerEntity)server.playerManager.players.get(i);
            //player.teleportToDimension(2);
            //player.networkHandler.teleport(0, 80, 0, player.yaw, player.pitch);
            //player.networkHandler.teleport(-1016, 24, 423, player.yaw, player.pitch);


            //for (Object entity : player.world.entities) {
            //    if (entity instanceof EnderDragonEntity) {
            //        ((EnderDragonEntity) entity).setHealth(1);
            //    }
            //}
        }
    }
}
