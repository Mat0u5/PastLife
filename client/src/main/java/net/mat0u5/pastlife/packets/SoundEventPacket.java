package net.mat0u5.pastlife.packets;

import net.mat0u5.pastlife.Main;
import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoundEventPacket extends Packet {
    public String soundName;
    public float volume;
    public float pitch;
    public boolean stream;

    public SoundEventPacket() {
    }

    public SoundEventPacket(String soundName, float volume, float pitch, boolean stream) {
        this.soundName = soundName;
        this.volume = volume;
        this.pitch = pitch;
        this.stream = stream;
    }

    public void read(DataInputStream input) {
        try {
            this.soundName = input.readUTF();
            this.volume = input.readFloat();
            this.pitch = input.readFloat();
            this.stream = input.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(DataOutputStream output) {
        try {
            output.writeUTF(this.soundName);
            output.writeFloat(this.volume);
            output.writeFloat(this.pitch);
            output.writeBoolean(this.stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(PacketHandler handler) {
        if (Main.minecraft == null) return;
        if (stream) {
            Main.minecraft.soundSystem.playRecord(soundName, (float) Main.minecraft.player.x, (float) Main.minecraft.player.y, (float) Main.minecraft.player.z, volume, pitch);
        }
        else {
            Main.minecraft.soundSystem.play(soundName, volume, pitch);
        }
    }

    public int getSize() {
        return 12 + soundName.length() * 2 + 4;
    }
}