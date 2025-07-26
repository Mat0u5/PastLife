package net.mat0u5.pastlife.lives;

public class ClientLivesManager {
    public static void handlePacket(LivesUpdatePacket packet) {
        System.out.println(packet.playerName + ": " + packet.lives + " lives.");
    }
}
