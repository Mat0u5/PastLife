package net.mat0u5.pastlife.client.utils;

import net.mat0u5.pastlife.client.MainClient;
import net.mat0u5.pastlife.client.lives.ClientLivesManager;
import net.mat0u5.pastlife.packets.LivesUpdatePacket;

public class ClientPacketHandler {

    public static void handleTitlePacket(TitlePacket packet) {
        if (MainClient.titleRenderer != null) {
            MainClient.titleRenderer.showTitle(packet.title, packet.subtitle, packet.fadeIn, packet.stay, packet.fadeOut);
        }
    }

    public static void handleLivesUpdatePacket(LivesUpdatePacket packet) {
        ClientLivesManager.handlePacket(packet);
    }
}
