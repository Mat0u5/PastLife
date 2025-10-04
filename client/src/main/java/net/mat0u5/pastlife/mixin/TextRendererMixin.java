package net.mat0u5.pastlife.mixin;

import net.minecraft.client.render.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @Redirect(
        method = "drawLayer(Ljava/lang/String;IIIZ)V",
        at = @At(
            value = "INVOKE", 
            target = "Ljava/lang/String;charAt(I)C"
        )
    )
    private char fixStringIndexOutOfBounds(String text, int index) {
        if (index >= text.length()) {
            return '\0';
        }
        return text.charAt(index);
    }
}
