package net.mat0u5.pastlife.utils;

import net.mat0u5.pastlife.Main;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.*;

import static net.mat0u5.pastlife.Main.server;

public class PlayerUtils {

    public static void sendPacketToAllPlayers(Packet packet) {
        if (server == null) return;
        server.getPlayerManager().sendToAll(packet);
    }

    public static void sendSubtitleToPlayer(ServerPlayerEntity player, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (server == null) return;
        if (player == null) return;
        TitleFadeS2CPacket fadePacket = new TitleFadeS2CPacket(fadeIn, stay, fadeOut);
        player.networkHandler.sendPacket(fadePacket);
        TitleS2CPacket titlePacket = new TitleS2CPacket(Text.of(""));
        player.networkHandler.sendPacket(titlePacket);
        SubtitleS2CPacket subtitlePacket = new SubtitleS2CPacket(Text.of(subtitle));
        player.networkHandler.sendPacket(subtitlePacket);
    }

    public static void sendTitleToPlayer(ServerPlayerEntity player, String title, int fadeIn, int duration, int fadeOut) {
        if (server == null) return;
        if (player == null) return;

        TitleFadeS2CPacket fadePacket = new TitleFadeS2CPacket(fadeIn, duration, fadeOut);
        player.networkHandler.sendPacket(fadePacket);
        TitleS2CPacket titlePacket = new TitleS2CPacket(Text.of(title));
        player.networkHandler.sendPacket(titlePacket);
    }

    public static void sendTitleToPlayers(Collection<ServerPlayerEntity> players, String title, int fadeIn, int stay, int fadeOut) {
        for (ServerPlayerEntity player : players) {
            sendTitleToPlayer(player, title, fadeIn, stay, fadeOut);
        }
    }

    public static void sendSubtitleToPlayers(Collection<ServerPlayerEntity> players, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (ServerPlayerEntity player : players) {
            sendSubtitleToPlayer(player, subtitle, fadeIn, stay, fadeOut);
        }
    }
    public static void sendSubtitleToAllPlayers(String subtitle, int fadeIn, int stay, int fadeOut) {
        sendSubtitleToPlayers(getAllPlayers(), subtitle, fadeIn, stay, fadeOut);
    }
    public static void sendTitleToAllPlayers(String title, int fadeIn, int stay, int fadeOut) {
        sendTitleToPlayers(getAllPlayers(), title, fadeIn, stay, fadeOut);
    }

    public static void playSoundToAllPlayers(SoundEvent sound, float volume, float pitch) {
        playSoundToPlayers(getAllPlayers(), sound, volume, pitch);
    }

    public static void playSoundToPlayers(Collection<ServerPlayerEntity> players, SoundEvent sound, float volume, float pitch) {
        playSoundToPlayers(players,sound, SoundCategory.MASTER, volume, pitch);
    }

    public static void playSoundToPlayers(Collection<ServerPlayerEntity> players, SoundEvent sound, SoundCategory soundCategory, float volume, float pitch) {
        for (ServerPlayerEntity player : players) {
            player.playSound(sound, soundCategory, volume, pitch);
            player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(sound.getId(), soundCategory, player.getPos(), volume, pitch));
        }
    }

    public static void playSoundToPlayer(ServerPlayerEntity player, SoundEvent sound, float volume, float pitch) {
        player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(sound.getId(), SoundCategory.MASTER, player.getPos(), volume, pitch));
    }

    public static List<ServerPlayerEntity> getAllPlayers() {
        if (server == null) return new ArrayList<>();
        return new ArrayList<>(server.getPlayerManager().getPlayerList());
    }

    public static ServerPlayerEntity getPlayer(String name) {
        if (server == null || name == null) return null;
        return server.getPlayerManager().getPlayer(name);
    }

    public static ServerPlayerEntity getPlayer(UUID uuid) {
        if (server == null || uuid == null) return null;
        return server.getPlayerManager().getPlayer(uuid);
    }

    public static void broadcastToPlayers(List<ServerPlayerEntity> players, String message) {
        for (ServerPlayerEntity player : players) {
            player.sendMessage(Text.of(message), false);
        }
    }

    public static void broadcast(String message) {
        broadcastToPlayers(getAllPlayers(), message);
    }

    public static void broadcastToAdmins(String message) {
        for (ServerPlayerEntity player : PlayerUtils.getAllPlayers()) {
            if (!isAdmin(player)) continue;
            player.sendMessage(Text.of(message), false);
        }
        Main.LOGGER.info(message);
    }

    public static boolean isAdmin(ServerPlayerEntity player) {
        if (server == null) return false;
        return server.getPlayerManager().isOperator(player.getGameProfile());
    }

    public static boolean isAdmin(ServerCommandSource source) {
        if (source.getEntity() == null) {
            return true;
        }
        if (source.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
            return isAdmin(player);
        }
        return false;
    }
}
