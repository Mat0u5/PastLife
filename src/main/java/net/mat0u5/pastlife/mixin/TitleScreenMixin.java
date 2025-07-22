package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.minecraft.client.gui.screen.TitleScreen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	private void init(CallbackInfo ci) {
		Main.LOGGER.info("This line is printed by an example mod mixin!");
	}
}