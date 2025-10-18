package com.mongoose.clanginghowl.client.render;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.render.model.FleshMaidenModel;
import com.mongoose.clanginghowl.common.entities.hostiles.FleshMaiden;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FleshMaidenRenderer<T extends FleshMaiden> extends MobRenderer<T, FleshMaidenModel<T>> {
    private static final ResourceLocation TEXTURE = ClangingHowl.location("textures/entity/flesh_maiden.png");

    public FleshMaidenRenderer(EntityRendererProvider.Context p_174403_) {
        super(p_174403_, new FleshMaidenModel<>(p_174403_.bakeLayer(CHModelLayer.FLESH_MAIDEN)), 0.5F);
    }

    public ResourceLocation getTextureLocation(T p_116009_) {
        return TEXTURE;
    }
}
