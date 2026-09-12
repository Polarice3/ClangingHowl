package com.mongoose.clanginghowl.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.world.ICHClientWorld;
import com.mongoose.clanginghowl.client.world.MeteorFlashState;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Stolen from @Smallinger's codes:https://github.com/Smallinger/Copper-Age-Backport/blob/1.20.1/common/src/main/java/com/github/smallinger/copperagebackport/mixin/client/LevelRendererMixin.java
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow protected abstract boolean doesMobEffectBlockSky(Camera p_234311_);

    @Unique
    private static final ResourceLocation FLASH_LOCATION = ClangingHowl.location("textures/environment/meteorite_explosion.png");

    @Unique
    private static final float FLASH_HEIGHT = 100.0F;

    @Unique
    private static final float FLASH_SCALE = 60.0F;

    @Inject(method = "renderSky", at = @At("RETURN"))
    private void clanginghowl$renderMeteorFlash(Matrix4f modelViewMatrix, Matrix4f p_254034_, float p_202426_, Camera p_202427_, boolean p_202428_, Runnable p_202429_, CallbackInfo ci) {
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(modelViewMatrix);
        ClientLevel level = this.minecraft.level;
        if (level == null) {
            return;
        }

        if (p_202428_) {
            return;
        }
        FogType fogtype = p_202427_.getFluidInCamera();
        if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !this.doesMobEffectBlockSky(p_202427_)) {
            if (level.dimension() == Level.OVERWORLD) {
                MeteorFlashState meteorFlashState = ICHClientWorld.get(level);
                if (meteorFlashState == null) {
                    return;
                }

                float intensity = meteorFlashState.getIntensity();

                // Only render if flash is active
                if (intensity <= 0.0001F) {
                    return;
                }

                // Check if lightning flash is hidden
                if (this.minecraft.options.hideLightningFlash().get()) {
                    return;
                }

                // While the ender dragon fog effect is active, the flash source in the sky is not visible
                if (this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
                    return;
                }

                float xAngle = meteorFlashState.getXAngle();
                float yAngle = meteorFlashState.getYAngle();

                // Render the flash quad - reusing the posestack from renderEndSky
                poseStack.pushPose();

                // Rotate to face the flash direction (like 1.21.10)
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yAngle));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - xAngle));

                // Move to flash position and scale
                poseStack.translate(0.0F, FLASH_HEIGHT, 0.0F);
                poseStack.scale(FLASH_SCALE, 1.0F, FLASH_SCALE);

                Matrix4f matrix4f = poseStack.last().pose();

                // Set up rendering state - use additive blending like 1.21.10 celestial shader
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 1, 1, 0); // SRC_ALPHA, ONE, ONE, ZERO
                RenderSystem.depthMask(false);
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderTexture(0, FLASH_LOCATION);

                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);


                bufferbuilder.addVertex(matrix4f, -1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F).setColor(intensity, intensity, intensity, intensity);
                bufferbuilder.addVertex(matrix4f, 1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F).setColor(intensity, intensity, intensity, intensity);
                bufferbuilder.addVertex(matrix4f, 1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F).setColor(intensity, intensity, intensity, intensity);
                bufferbuilder.addVertex(matrix4f, -1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F).setColor(intensity, intensity, intensity, intensity);

                com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

                RenderSystem.depthMask(true);
                RenderSystem.defaultBlendFunc();

                poseStack.popPose();
            }
        }
    }
}
