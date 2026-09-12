package com.mongoose.clanginghowl.mixin;

import com.mongoose.clanginghowl.client.render.MeteorShowerFogRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

//Stolen from @yungnickyoung codes:https://github.com/YUNG-GANG/YUNGs-Cave-Biomes/blob/1.20.1/Forge/src/main/java/com/yungnickyoung/minecraft/yungscavebiomes/mixin/client/MixinFogRendererForge.java
@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;

    @Unique
    private static final MeteorShowerFogRenderer clangingHowl$meteorShowerFogRenderer = new MeteorShowerFogRenderer();

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void clanginghowl_handleMeteorShowerFog(Camera camera, FogRenderer.FogMode mode, float renderDistance, boolean isVeryFoggy, float partialTicks, CallbackInfo ci) {
        clangingHowl$meteorShowerFogRenderer.render(mode, renderDistance);
    }

    @Inject(method = "setupColor", at = @At("TAIL"))
    private static void clanginghowl_setupMeteorShowerColor(Camera camera, float f, ClientLevel clientLevel, int i2, float g, CallbackInfo ci) {        // Interpolate fog if needed
        if (clangingHowl$meteorShowerFogRenderer.getFogLevel() > 0) {
            Color color = Color.decode(String.valueOf(0x5d5551));
            fogRed = (float) Mth.lerp(clangingHowl$meteorShowerFogRenderer.getFogLevel(), fogRed, color.getRed() / 255.0F);
            fogGreen = (float) Mth.lerp(clangingHowl$meteorShowerFogRenderer.getFogLevel(), fogGreen, color.getGreen() / 255.0F);
            fogBlue = (float) Mth.lerp(clangingHowl$meteorShowerFogRenderer.getFogLevel(), fogBlue, color.getBlue() / 255.0F);
            com.mojang.blaze3d.systems.RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);
        }
    }
}
