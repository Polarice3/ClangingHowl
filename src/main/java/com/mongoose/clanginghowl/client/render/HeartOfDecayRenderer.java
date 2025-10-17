package com.mongoose.clanginghowl.client.render;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.render.model.HeartOfDecayModel;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HeartOfDecayRenderer<T extends HeartOfDecay> extends MobRenderer<T, HeartOfDecayModel<T>> {
    private static final ResourceLocation SPIDER_LOCATION = ClangingHowl.location("textures/entity/heart_of_decay.png");

    public HeartOfDecayRenderer(EntityRendererProvider.Context p_174403_) {
        super(p_174403_, new HeartOfDecayModel<>(p_174403_.bakeLayer(CHModelLayer.HEART_OF_DECAY)), 0.4F);
    }

    protected float getFlipDegrees(T p_116011_) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T p_116009_) {
        return SPIDER_LOCATION;
    }
}
