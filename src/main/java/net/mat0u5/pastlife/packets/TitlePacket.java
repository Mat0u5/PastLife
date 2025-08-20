package net.mat0u5.pastlife.packets;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.client.utils.ClientPacketHandler;
import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TitlePacket extends Packet {
    public String title;
    public String subtitle;
    public int fadeIn;
    public int stay;
    public int fadeOut;

    public TitlePacket() {
    }

    public TitlePacket(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void read(DataInputStream input) {
        try {
            this.title = input.readUTF();
            this.subtitle = input.readUTF();
            this.fadeIn = input.readInt();
            this.stay = input.readInt();
            this.fadeOut = input.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(DataOutputStream output) {
        try {
            output.writeUTF(this.title);
            output.writeUTF(this.subtitle);
            output.writeInt(this.fadeIn);
            output.writeInt(this.stay);
            output.writeInt(this.fadeOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(PacketHandler handler) {
        if (Main.isClient()) {
            ClientPacketHandler.handleTitlePacket(this);
        }
    }

    public int getSize() {
        return 4 + title.length() * 2 + subtitle.length() * 2 + 12;
    }
}
