package net.mat0u5.pastlife.packets;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WorldBorderUpdatePacket extends Packet {
    public int centerX;
    public int centerZ;
    public int size;

    public WorldBorderUpdatePacket() {
    }

    public WorldBorderUpdatePacket(int centerX, int centerZ, int size) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.size = size;
    }

    public void read(DataInputStream input) {
        try {
            this.centerX = input.readInt();
            this.centerZ = input.readInt();
            this.size = input.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(DataOutputStream output) {
        try {
            output.writeInt(this.centerX);
            output.writeInt(this.centerZ);
            output.writeInt(this.size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(PacketHandler handler) {
    }

    public int getSize() {
        return 12;
    }
}
