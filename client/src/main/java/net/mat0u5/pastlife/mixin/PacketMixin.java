package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.LivesUpdatePacket;
import net.mat0u5.pastlife.packets.SoundEventPacket;
import net.mat0u5.pastlife.packets.TitlePacket;
import net.mat0u5.pastlife.packets.WorldBorderUpdatePacket;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

@Mixin(Packet.class)
public class PacketMixin {
    @Shadow
    private static Map ID_TO_TYPE;
    @Shadow
    private static Map TYPE_TO_ID;
    @Shadow
    private static Set S2C;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerCustomPackets(CallbackInfo ci) {
        registerPacket(249, LivesUpdatePacket.class);
        registerPacket(248, WorldBorderUpdatePacket.class);
        registerPacket(247, SoundEventPacket.class);
        registerPacket(246, TitlePacket.class);
    }

    private static void registerPacket(int id, Class packetClass) {
        ID_TO_TYPE.put(id, packetClass);
        TYPE_TO_ID.put(packetClass, id);
        S2C.add(id);
        Main.log("Registered CustomPacket with ID "+id+" for "+packetClass);
    }
}
