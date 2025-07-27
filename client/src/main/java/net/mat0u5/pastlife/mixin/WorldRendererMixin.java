package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private Minecraft minecraft;
    @Shadow
    private World world;
    private static int frameCounter = 0;
    private static boolean shouldRenderBorder = false;
    private static double distToBorder = 200;

    @Inject(method = "render(IIID)I", at = @At("RETURN"))
    private void renderWorldBorder(int renderStartIndex, int chunksToRender, int renderStage, double tickDelta, CallbackInfoReturnable<Integer> cir) {
        if (renderStage != 0) {
            return;
        }

        PlayerEntity player = minecraft.player;
        if (player == null) {
            return;
        }

        if (!WorldBorderManager.initialized || player.world.dimension.isNether) {
            return;
        }

        frameCounter++;

        double centerX = WorldBorderManager.centerX;
        double centerZ = WorldBorderManager.centerZ;

        if (frameCounter % 10 == 0) {
            double playerX = player.x;
            double playerZ = player.z;

            double halfSize = WorldBorderManager.borderSize / 2.0;

            distToBorder = Math.min(
                    Math.abs(playerX - (centerX - halfSize)),
                    Math.min(Math.abs(playerX - (centerX + halfSize)),
                            Math.min(Math.abs(playerZ - (centerZ - halfSize)),
                                    Math.abs(playerZ - (centerZ + halfSize))))
            );

            shouldRenderBorder = distToBorder < 32;
            frameCounter = 0;
        }

        if (shouldRenderBorder) {
            double halfSize = WorldBorderManager.borderSize / 2.0;
            renderBorderWalls(centerX, centerZ, halfSize, tickDelta);
        }
    }

    private void renderBorderWalls(double centerX, double centerZ, double halfSize, double tickDelta) {
        PlayerEntity player = minecraft.player;
        if (player == null) return;

        double playerX = player.prevX + (player.x - player.prevX) * tickDelta;
        double playerY = player.prevY + (player.y - player.prevY) * tickDelta;
        double playerZ = player.prevZ + (player.z - player.prevZ) * tickDelta;

        double offset = 0.03;

        double minX = centerX - halfSize + offset;
        double maxX = centerX + halfSize - offset;
        double minZ = centerZ - halfSize + offset;
        double maxZ = centerZ + halfSize - offset;

        double maxDistance = Math.max(10, 64-distToBorder*2);

        boolean renderNorth = (playerZ - (centerZ - halfSize)) < 32;
        boolean renderSouth = ((centerZ + halfSize) - playerZ) < 32;
        boolean renderWest = (playerX - (centerX - halfSize)) < 32;
        boolean renderEast = ((centerX + halfSize) - playerX) < 32;

        if (!renderNorth && !renderSouth && !renderWest && !renderEast) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(0.0f, 0.5f, 1.0f, 0.2f);

        GL11.glTranslated(-playerX, -playerY, -playerZ);

        double minY = Math.max(0, playerY - 32);
        double maxY = Math.min(128, playerY + 32);

        GL11.glBegin(GL11.GL_QUADS);

        if (renderNorth) {
            // North wall (negative Z)
            double wallMinX = Math.max(minX, playerX - maxDistance);
            double wallMaxX = Math.min(maxX, playerX + maxDistance);

            GL11.glVertex3d(wallMinX, minY, minZ);
            GL11.glVertex3d(wallMaxX, minY, minZ);
            GL11.glVertex3d(wallMaxX, maxY, minZ);
            GL11.glVertex3d(wallMinX, maxY, minZ);
        }

        if (renderSouth) {
            // South wall (positive Z)
            double wallMinX = Math.max(minX, playerX - maxDistance);
            double wallMaxX = Math.min(maxX, playerX + maxDistance);

            GL11.glVertex3d(wallMaxX, minY, maxZ);
            GL11.glVertex3d(wallMinX, minY, maxZ);
            GL11.glVertex3d(wallMinX, maxY, maxZ);
            GL11.glVertex3d(wallMaxX, maxY, maxZ);
        }

        if (renderWest) {
            // West wall (negative X)
            double wallMinZ = Math.max(minZ, playerZ - maxDistance);
            double wallMaxZ = Math.min(maxZ, playerZ + maxDistance);

            GL11.glVertex3d(minX, minY, wallMaxZ);
            GL11.glVertex3d(minX, minY, wallMinZ);
            GL11.glVertex3d(minX, maxY, wallMinZ);
            GL11.glVertex3d(minX, maxY, wallMaxZ);
        }

        if (renderEast) {
            // East wall (positive X)
            double wallMinZ = Math.max(minZ, playerZ - maxDistance);
            double wallMaxZ = Math.min(maxZ, playerZ + maxDistance);

            GL11.glVertex3d(maxX, minY, wallMinZ);
            GL11.glVertex3d(maxX, minY, wallMaxZ);
            GL11.glVertex3d(maxX, maxY, wallMaxZ);
            GL11.glVertex3d(maxX, maxY, wallMinZ);
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
