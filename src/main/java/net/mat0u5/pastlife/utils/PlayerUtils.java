package net.mat0u5.pastlife.utils;

import net.mat0u5.pastlife.Main;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.SoundEventS2CPacket;
import net.minecraft.network.packet.s2c.play.TitlesS2CPacket;
import net.minecraft.resource.Identifier;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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
        player.networkHandler.sendPacket(new SoundEventS2CPacket(new SoundEvent(new Identifier("pastlife_"+sound)), SoundCategory.MASTER, player.x, player.y, player.z, volume, pitch));
    }
    public static void playSoundToPlayers(List<ServerPlayerEntity> players, String sound, float volume, float pitch) {
        for (ServerPlayerEntity player : players) {
            playSoundToPlayer(player, sound, volume, pitch);
        }
    }

    public static void sendTitleToAllPlayers(String title, int fadeIn, int duration, int fadeOut) {
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.CLEAR, new LiteralText(title), fadeIn, duration, fadeOut));
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.TITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendSubtitleToAllPlayers(String title, int fadeIn, int duration, int fadeOut) {
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.CLEAR, new LiteralText(title), fadeIn, duration, fadeOut));
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.TITLE, new LiteralText(""), fadeIn, duration, fadeOut));
        sendPacketToAllPlayers(new TitlesS2CPacket(TitlesS2CPacket.Type.SUBTITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendTitleToPlayer(ServerPlayerEntity player, String title, int fadeIn, int duration, int fadeOut) {
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.CLEAR, new LiteralText(title), fadeIn, duration, fadeOut));
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.TITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendSubtitleToPlayer(ServerPlayerEntity player, String title, int fadeIn, int duration, int fadeOut) {
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.CLEAR, new LiteralText(title), fadeIn, duration, fadeOut));
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.TITLE, new LiteralText(""), fadeIn, duration, fadeOut));
        player.networkHandler.sendPacket(new TitlesS2CPacket(TitlesS2CPacket.Type.SUBTITLE, new LiteralText(title), fadeIn, duration, fadeOut));
    }
    public static void sendTitleToPlayers(List<ServerPlayerEntity> players, String title, int fadeIn, int duration, int fadeOut) {
        for (ServerPlayerEntity player : players) {
            sendTitleToPlayer(player, title, fadeIn, duration, fadeOut);
        }
    }
    public static void sendSubtitleToPlayers(List<ServerPlayerEntity> players, String title, int fadeIn, int duration, int fadeOut) {
        for (ServerPlayerEntity player : players) {
            sendSubtitleToPlayer(player, title, fadeIn, duration, fadeOut);
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
}
