package net.mat0u5.pastlife.mixin;

import net.minecraft.client.sound.system.SoundFile;
import net.minecraft.client.sound.system.Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(Sounds.class)
public class SoundsMixin {
    @Inject(method = "load", at = @At("HEAD"))
    private void modifySkinField(String path, File file, CallbackInfoReturnable<SoundFile> cir) {
        //TODO REMOVE
        System.out.println("Loading sound file: " + path + " from " + file.getAbsolutePath());
    }
}
