package net.mat0u5.pastlife.packets;

import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoundEventPacket extends Packet {
    public String soundName;
    public float volume;
    public float pitch;

    public SoundEventPacket() {
    }

    public SoundEventPacket(String soundName, float volume, float pitch) {
        this.soundName = soundName;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void read(DataInputStream input) {
        try {
            this.soundName = input.readUTF();
            this.volume = input.readFloat();
            this.pitch = input.readFloat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(DataOutputStream output) {
        try {
            output.writeUTF(this.soundName);
            output.writeFloat(this.volume);
            output.writeFloat(this.pitch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(PacketHandler handler) {
    }

    public int getSize() {
        return 8 + soundName.length() * 2 + 4;
    }
}