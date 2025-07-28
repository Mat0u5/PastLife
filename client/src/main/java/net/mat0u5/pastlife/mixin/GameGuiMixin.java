package net.mat0u5.pastlife.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.lives.ClientLivesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.network.PlayerInfo;
import net.minecraft.client.render.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameGui.class)
public class GameGuiMixin {
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void renderTitle(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci) {
        if (Main.titleRenderer != null) {
            Main.titleRenderer.renderTitle(minecraft, tickDelta);
        }
    }

    @Unique
    String currentPlayerName;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;drawWithShadow(Ljava/lang/String;III)V", ordinal = 6))
    private void coloredName(TextRenderer instance, String playerName, int x, int y, int color) {
        currentPlayerName = playerName;
        String colorCode = ClientLivesManager.getColorCode(playerName);
        if (colorCode != null) {
            playerName = colorCode + playerName;
        }
        instance.drawWithShadow(playerName, x, y, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;drawTexture(IIIIII)V", ordinal = 20))
    private void livesNum(GameGui instance, int x, int y, int u, int v, int width, int height) {
        instance.drawTexture(x, y, u, v, width, height);

        if (currentPlayerName == null) {
            return;
        }

        Integer lives = ClientLivesManager.getPlayerLives(currentPlayerName);
        String livesStr = "";
        if (lives != null) {
            if (lives < 4) {
                livesStr = String.valueOf(lives);
            }
            else {
                livesStr = "4+";
            }
        }
        String text = "§e"+livesStr;
        minecraft.textRenderer.drawWithShadow(text, x - minecraft.textRenderer.getWidth(text)-2, y, 0xFFFFFF);
    }
}
