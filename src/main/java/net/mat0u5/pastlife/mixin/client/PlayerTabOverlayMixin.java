package net.mat0u5.pastlife.mixin.client;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListHud.class)
public class PlayerTabOverlayMixin {

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/scoreboard/ReadableScoreboardScore;getFormattedScore(Lnet/minecraft/scoreboard/ReadableScoreboardScore;Lnet/minecraft/scoreboard/number/NumberFormat;)Lnet/minecraft/text/MutableText;"))
    private MutableText modifyFormattedScore(ReadableScoreboardScore readableScoreboardScore, NumberFormat numberFormat) {
        MutableText originalText = ReadableScoreboardScore.getFormattedScore(readableScoreboardScore, numberFormat);
        if (readableScoreboardScore == null || originalText == null) return originalText;

        int score = readableScoreboardScore.getScore();
        if (score >= 4) {
            return Text.literal("4+").setStyle(originalText.getStyle());
        }

        return originalText;
    }
}
