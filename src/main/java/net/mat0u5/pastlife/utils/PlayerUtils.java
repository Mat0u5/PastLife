package net.mat0u5.pastlife.utils;

import net.mat0u5.pastlife.Main;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.SoundEventS2CPacket;
import net.minecraft.network.packet.s2c.play.TitlesS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;

import java.util.List;

public class PlayerUtils {
    public static void broadcast(String message) {
        if (Main.server == null) {
            return;
        }
        Main.server.getPlayerManager().sendMessage(new LiteralText(message), false);
    }

    public static void broadcastToPlayer(ServerPlayerEntity player, String message) {
        player.sendMessage(new LiteralText(message));
    }

    public static void broadcastToPlayers(List<ServerPlayerEntity> players, String message) {
        for (ServerPlayerEntity player : players) {
            broadcastToPlayer(player, message);
        }
    }

    public static void playSoundToAllPlayers(String sound, float volume, float pitch) {
        if (Main.server == null) {
            return;
        }
        playSoundToPlayers(Main.server.getPlayerManager().getAll(), sound, volume, pitch);
    }
    public static void playSoundToPlayer(ServerPlayerEntity player, String sound, float volume, float pitch) {
        player.networkHandler.sendPacket(new SoundEventS2CPacket(sound, SoundCategory.MASTER, player.x, player.y, player.z, volume, pitch));
    }
    public static void playSoundToPlayers(List<ServerPlayerEntity> players, String sound, float volume, float pitch) {
        for (ServerPlayerEntity player : players) {
            playSoundToPlayer(player, sound, volume, pitch);
        }
    }

    public static void sendTitleToAllPlayers(String title, int fadeIn, int duration, int fadeOut) {
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.TITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendSubitleToAllPlayers(String title, int fadeIn, int duration, int fadeOut) {
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.SUBTITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendTitleToPlayer(ServerPlayerEntity player, String title, int fadeIn, int duration, int fadeOut) {
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.TITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendSubitleToPlayer(ServerPlayerEntity player, String title, int fadeIn, int duration, int fadeOut) {
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.SUBTITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendTitleToPlayers(List<ServerPlayerEntity> players, String title, int fadeIn, int duration, int fadeOut) {
        for (ServerPlayerEntity player : players) {
            sendTitleToPlayer(player, title, fadeIn, duration, fadeOut);
        }
    }
    public static void sendSubitleToPlayers(List<ServerPlayerEntity> players, String title, int fadeIn, int duration, int fadeOut) {
        for (ServerPlayerEntity player : players) {
            sendSubitleToPlayer(player, title, fadeIn, duration, fadeOut);
        }
    }

    public static void sendPacketToAllPlayers(Packet packet) {
        if (Main.server == null) {
            return;
        }
        Main.server.getPlayerManager().sendPacket(packet);
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
        for(ServerPlayerEntity player :  server.getPlayerManager().getAll()) {

            //player.setHealth(1);

            //player.world.addEntity(new XpOrbEntity(player.world, player.x, player.y, player.z, 10000));

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
