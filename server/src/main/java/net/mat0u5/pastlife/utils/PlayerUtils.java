package net.mat0u5.pastlife.utils;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.SoundEventPacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import java.util.List;

public class PlayerUtils {
    public static void playSoundToAllPlayers(String sound, float volume, float pitch) {
        if (Main.server == null) {
            return;
        }
        Main.server.playerManager.sendPacket(new SoundEventPacket(sound, volume, pitch));
    }

    public static void playSoundToPlayer(ServerPlayerEntity player, String sound, float volume, float pitch) {
        player.networkHandler.sendPacket(new SoundEventPacket(sound, volume, pitch));
    }


    public static void playSoundToPlayers(List<ServerPlayerEntity> player, String sound, float volume, float pitch) {
        for (ServerPlayerEntity serverPlayerEntity : player) {
            playSoundToPlayer(serverPlayerEntity, sound, volume, pitch);
        }
    }
}
