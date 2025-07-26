package net.mat0u5.pastlife.lives;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LivesUpdatePacket extends Packet {
    public String playerName;
    public int lives;

    public LivesUpdatePacket() {
    }

    public LivesUpdatePacket(String playerName, int lives) {
        this.playerName = playerName;
        this.lives = lives;
    }

    public void read(DataInputStream input) {
        try {
            this.playerName = input.readUTF();
            this.lives = input.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(DataOutputStream output) {
        try {
            output.writeUTF(this.playerName);
            output.writeInt(this.lives);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(PacketHandler handler) {
        ClientLivesManager.handlePacket(this);
    }

    public int getSize() {
        return 4 + playerName.length() * 2 + 4;
    }
}