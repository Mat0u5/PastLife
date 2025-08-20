package net.mat0u5.pastlife.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Window;
import org.lwjgl.opengl.GL11;

public class TitleRenderer {
    private static final int[] COLOR_VALUES = {
            0x000000, // §0 - Black
            0x0000AA, // §1 - Dark Blue
            0x00AA00, // §2 - Dark Green
            0x00AAAA, // §3 - Dark Aqua
            0xAA0000, // §4 - Dark Red
            0xAA00AA, // §5 - Dark Purple
            0xFFAA00, // §6 - Gold
            0xAAAAAA, // §7 - Gray
            0x555555, // §8 - Dark Gray
            0x5555FF, // §9 - Blue
            0x55FF55, // §a - Green
            0x55FFFF, // §b - Aqua
            0xFF5555, // §c - Red
            0xFF55FF, // §d - Light Purple
            0xFFFF55, // §e - Yellow
            0xFFFFFF  // §f - White
    };

    private String currentTitle = "";
    private String currentSubtitle = "";
    private int fadeInTime = 0;
    private int stayTime = 0;
    private int fadeOutTime = 0;
    private int startTick = 0;
    private boolean isActive = false;

    public static int getTextColor(String text) {
        if (text.length() < 2 || text.charAt(0) != '§') {
            return 0xFFFFFF;
        }

        char colorChar = text.charAt(1);

        if (colorChar >= '0' && colorChar <= '9') {
            return COLOR_VALUES[colorChar - '0'];
        } else if (colorChar >= 'a' && colorChar <= 'f') {
            return COLOR_VALUES[colorChar - 'a' + 10];
        } else if (colorChar >= 'A' && colorChar <= 'F') {
            return COLOR_VALUES[colorChar - 'A' + 10];
        }

        return 0xFFFFFF;
    }

    public void showTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.currentTitle = title;
        this.currentSubtitle = subtitle;
        this.fadeInTime = fadeIn;
        this.stayTime = stay;
        this.fadeOutTime = fadeOut;
        this.startTick = 0;
        this.isActive = true;
    }

    public void renderTitle(Minecraft mc, float tickDelta) {
        if (!isActive) return;
        if (startTick == 0) return;

        float currentProgress = startTick + tickDelta;
        int totalTime = fadeInTime + stayTime + fadeOutTime;

        if (currentProgress >= totalTime-1) {
            isActive = false;
            return;
        }

        float alpha = calculateAlpha(currentProgress);
        int alpha255 = (int) (alpha*255);

        Window window = new Window(mc.options, mc.width, mc.height);
        int width = window.getWidth();
        int height = window.getHeight();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();

        if (!currentTitle.isEmpty()) {
            String rawTitle = currentTitle;
            if (rawTitle.startsWith("§")) {
                rawTitle = rawTitle.substring(2);
            }

            float titleScale = 3.0f;
            GL11.glPushMatrix();
            GL11.glScalef(titleScale, titleScale, 1.0f);

            int titleWidth = mc.textRenderer.getWidth(rawTitle);
            int scaledX = (int)((width - titleWidth * titleScale) / (2 * titleScale));
            int scaledY = (int)((height / 2 - 20) / titleScale);

            mc.textRenderer.drawWithShadow(rawTitle, scaledX, scaledY,
                    getTextColor(currentTitle) + (alpha255 << 24));
            GL11.glPopMatrix();
        }

        if (!currentSubtitle.isEmpty()) {
            String rawSubtitle = currentSubtitle;
            if (rawSubtitle.startsWith("§")) {
                rawSubtitle = rawSubtitle.substring(2);
            }

            float subtitleScale = 2.0f;
            GL11.glPushMatrix();
            GL11.glScalef(subtitleScale, subtitleScale, 1.0f);

            int subtitleWidth = mc.textRenderer.getWidth(rawSubtitle);
            int scaledX = (int)((width - subtitleWidth * subtitleScale) / (2 * subtitleScale));
            int scaledY = (int)((height / 2 + 30) / subtitleScale);

            mc.textRenderer.drawWithShadow(rawSubtitle, scaledX, scaledY,
                    getTextColor(currentSubtitle) + (alpha255 << 24));
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void tick() {
        if (isActive) {
            startTick++;
        }
    }

    private float calculateAlpha(float currentProgress) {
        if (currentProgress < fadeInTime) {
            return currentProgress / fadeInTime;
        } else if (currentProgress < fadeInTime + stayTime) {
            return 1.0f;
        } else {
            float fadeOutStart = fadeInTime + stayTime;
            float fadeOutProgress = currentProgress - fadeOutStart;
            return Math.max(0.0f, 1.0f - (fadeOutProgress / fadeOutTime));
        }
    }
}