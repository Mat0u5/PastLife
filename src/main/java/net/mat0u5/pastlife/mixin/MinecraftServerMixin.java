package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.secretsociety.SecretSociety;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onModInit(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        if (!Main.initializedCommands) {
            Main.initializedCommands = true;
            server.getGameRules().get(GameRules.KEEP_INVENTORY).set(true, server);

            WorldBorder border = server.getOverworld().getWorldBorder();
            if (border.getSize() > 1_000_000) {
                border.setSize(400);
            }
            BlockPos spawn = server.getOverworld().getSpawnPos();
            border.setCenter(spawn.getX(), spawn.getZ());
            Main.log("Initializing World Border: "+border.getSize()+"_" + spawn.getX() + "_" + spawn.getZ());

            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"scoreboard objectives add Lives dummy");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"scoreboard objectives setdisplay list Lives");

            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team add DarkGreen");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team add Green");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team add Yellow");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team add Red");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team add Dead");

            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team modify DarkGreen color dark_green");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team modify Green color green");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team modify Yellow color yellow");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team modify Red color red");
            server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"team modify Dead color dark_gray");
        }

        TaskScheduler.onTick();
        SecretSociety.tick(server);
    }
}
