package com.mongoose.clanginghowl.client.render;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.render.model.ExtraterrestrialReaperModel;
import com.mongoose.clanginghowl.common.entities.hostiles.ExReaper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ExReaperRenderer<T extends ExReaper> extends MobRenderer<T, ExtraterrestrialReaperModel<T>> {
    private static final ResourceLocation TEXTURE = ClangingHowl.location("textures/entity/extraterrestrial_reaper.png");

    public ExReaperRenderer(EntityRendererProvider.Context p_174403_) {
        super(p_174403_, new ExtraterrestrialReaperModel<>(p_174403_.bakeLayer(CHModelLayer.EXTRATERRESTRIAL_REAPER)), 0.5F);
        this.addLayer(new GlowLayer<>(this));
    }

    public ResourceLocation getTextureLocation(T p_116009_) {
        return TEXTURE;
    }

    public static class GlowLayer<T extends LivingEntity> extends EyesLayer<T, ExtraterrestrialReaperModel<T>> {
        private static final RenderType GLOW = RenderType.eyes(ClangingHowl.location("textures/entity/extraterrestrial_reaper_e.png"));

        public GlowLayer(RenderLayerParent<T, ExtraterrestrialReaperModel<T>> p_116964_) {
            super(p_116964_);
        }

        public RenderType renderType() {
            return GLOW;
        }
    }
}
