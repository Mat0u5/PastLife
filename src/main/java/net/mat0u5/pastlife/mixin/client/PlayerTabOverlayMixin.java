package net.mat0u5.pastlife.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.PlayerTabOverlay;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.text.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Shadow
    private Minecraft minecraft;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 1))
    private int clampScore(TextRenderer instance, String text) {
        return instance.getWidth(text+"+");
    }

    @Redirect(method = "renderDisplayScore", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I", ordinal = 1))
    private int clampScore(TextRenderer instance, String text, float x, float y, int color) {
        int originalWidth = minecraft.textRenderer.getWidth(text);
        try {
            String copy = text.replace(Formatting.YELLOW.toString(), "");
            int score = Integer.parseInt(copy);
            if (score >= 4) {
                text = Formatting.YELLOW + "4+";
            }
        }catch(Exception ignored) {}
        int newWidth = minecraft.textRenderer.getWidth(text);
        return instance.drawWithShadow(text, x - (newWidth - originalWidth), y, color);
    }
}
