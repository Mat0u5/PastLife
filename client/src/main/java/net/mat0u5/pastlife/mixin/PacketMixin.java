package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.lives.LivesUpdatePacket;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Packet.class)
public class PacketMixin {
    @Shadow
    private static Map ID_TO_TYPE;
    @Shadow
    private static Map TYPE_TO_ID;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerCustomPackets(CallbackInfo ci) {
        registerPacket(250, LivesUpdatePacket.class);
    }

    private static void registerPacket(int id, Class packetClass) {
        ID_TO_TYPE.put(id, packetClass);
        TYPE_TO_ID.put(packetClass, id);
        System.out.println("Registered CustomPacket with ID "+id+" for "+packetClass);
    }
}
