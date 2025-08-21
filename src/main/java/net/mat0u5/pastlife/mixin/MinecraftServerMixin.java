package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.secretsociety.SecretSociety;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "getResourcePackUrl", at = @At("HEAD"), cancellable = true)
    private void getResourcePackUrl(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(Main.RESOURCEPACK_URL);
    }

    @Inject(method = "getResourcePackHash", at = @At("HEAD"), cancellable = true)
    private void getResourcePackHash(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(Main.RESOURCEPACK_HASH);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onModInit(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (!Main.initializedCommands) {
            Main.initializedCommands = true;
            ServerWorld overworld = server.getWorld(0);
            overworld.getGameRules().set("keepInventory", "true");

            WorldBorder border = overworld.getWorldBorder();
            if (border.getLerpSize() > 1_000_000) {
                border.setSize(400);
                BlockPos spawn = overworld.getSpawnPoint();
                border.setCenter(spawn.getX(), spawn.getZ());
                Main.log("Initializing World Border: 400, " + spawn.getX() + "_" + spawn.getZ());
            }

            //TODO test
            server.getCommandHandler().run(server, "scoreboard objectives add Lives dummy");
            server.getCommandHandler().run(server, "scoreboard objectives setdisplay sidebar Lives");//TODO remove
            server.getCommandHandler().run(server, "scoreboard objectives setdisplay list Lives");
            server.getCommandHandler().run(server, "scoreboard teams add DarkGreen");
            server.getCommandHandler().run(server, "scoreboard teams add Green");
            server.getCommandHandler().run(server, "scoreboard teams add Yellow");
            server.getCommandHandler().run(server, "scoreboard teams add Red");
            server.getCommandHandler().run(server, "scoreboard teams add Dead");
        }

        TaskScheduler.onTick();
        SecretSociety.tick(server);
    }
}
