package net.mat0u5.pastlife.client.utils;

import net.mat0u5.pastlife.client.MainClient;
import net.mat0u5.pastlife.client.lives.ClientLivesManager;
import net.mat0u5.pastlife.packets.LivesUpdatePacket;
import net.mat0u5.pastlife.packets.TitlePacket;
import net.mat0u5.pastlife.packets.WorldBorderUpdatePacket;
import net.mat0u5.pastlife.utils.WorldBorderManager;

public class ClientPacketHandler {
    public static void handleWorldBorderUpdatePacket(WorldBorderUpdatePacket packet) {
        WorldBorderManager.init(packet.size, packet.centerX, packet.centerZ);
    }

    public static void handleTitlePacket(TitlePacket packet) {
        if (MainClient.titleRenderer != null) {
            MainClient.titleRenderer.showTitle(packet.title, packet.subtitle, packet.fadeIn, packet.stay, packet.fadeOut);
        }
    }

    public static void handleSoundEventPacket(SoundEventPacket packet) {
        if (MainClient.minecraft == null) return;
        if (packet.stream) {
            MainClient.minecraft.soundSystem.playRecord(packet.soundName, (float) MainClient.minecraft.player.x, (float) MainClient.minecraft.player.y, (float) MainClient.minecraft.player.z, packet.volume, packet.pitch);
        }
        else {
            MainClient.minecraft.soundSystem.play(packet.soundName, packet.volume, packet.pitch);
        }
    }

    public static void handleLivesUpdatePacket(LivesUpdatePacket packet) {
        ClientLivesManager.handlePacket(packet);
    }
}
