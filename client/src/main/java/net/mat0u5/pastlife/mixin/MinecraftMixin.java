package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.KeybindUtils;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void onModInit(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        Main.init(minecraft);
    }

    @Unique
    boolean previousDebugEnabled = false;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tickHead(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        previousDebugEnabled = minecraft.options.debugEnabled;
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickTail(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        if (minecraft.options.debugEnabled != previousDebugEnabled) {
            KeybindUtils.onF5Pressed(minecraft);
        }
    }
}
