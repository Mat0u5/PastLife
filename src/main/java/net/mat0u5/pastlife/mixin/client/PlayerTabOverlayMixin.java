package net.mat0u5.pastlife.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListHud.class)
public class PlayerTabOverlayMixin {
    @Shadow
    private MinecraftClient client;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I"))
    private int clampScore(TextRenderer instance, String text) {
        return instance.getWidth(text+"+");
    }

    @Redirect(method = "renderScoreboardObjective", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I", ordinal = 1))
    private int clampScore(TextRenderer instance, MatrixStack matrices, String text, float x, float y, int color) {
        int originalWidth = client.textRenderer.getWidth(text);
        try {
            String copy = text.replace(Formatting.YELLOW.toString(), "");
            int score = Integer.parseInt(copy);
            if (score >= 4) {
                text = Formatting.YELLOW + "4+";
            }
        }catch(Exception ignored) {}
        int newWidth = client.textRenderer.getWidth(text);
        return instance.drawWithShadow(matrices, text, x - (newWidth - originalWidth), y, color);
    }
}
