package com.mongoose.clanginghowl.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.render.model.ProwlerModel;
import com.mongoose.clanginghowl.common.entities.hostiles.Prowler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

import javax.annotation.Nullable;

public class ProwlerRenderer<T extends Prowler> extends MobRenderer<T, ProwlerModel<T>> {
    private static final ResourceLocation TEXTURE = ClangingHowl.location("textures/entity/prowler.png");

    public ProwlerRenderer(EntityRendererProvider.Context p_174403_) {
        super(p_174403_, new ProwlerModel<>(p_174403_.bakeLayer(CHModelLayer.PROWLER)), 0.0F);
        this.addLayer(new GlowLayer<>(this));
        this.addLayer(new OutlineLayer<>(this));
    }

    @Override
    public void render(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        if (p_115308_.tickCount < 2 && p_115308_.getPose() == Pose.EMERGING) {
            return;
        }
        if (p_115308_.becomeInvisible > 0) {
            p_115311_.pushPose();
            this.model.attackTime = this.getAttackAnim(p_115308_, p_115310_);

            boolean shouldSit = p_115308_.isPassenger() && (p_115308_.getVehicle() != null && p_115308_.getVehicle().shouldRiderSit());
            this.model.riding = shouldSit;
            this.model.young = p_115308_.isBaby();
            float f = Mth.rotLerp(p_115310_, p_115308_.yBodyRotO, p_115308_.yBodyRot);
            float f1 = Mth.rotLerp(p_115310_, p_115308_.yHeadRotO, p_115308_.yHeadRot);
            float f2 = f1 - f;
            if (shouldSit && p_115308_.getVehicle() instanceof LivingEntity livingentity) {
                f = Mth.rotLerp(p_115310_, livingentity.yBodyRotO, livingentity.yBodyRot);
                f2 = f1 - f;
                float f3 = Mth.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }

                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }

                f2 = f1 - f;
            }

            float f6 = Mth.lerp(p_115310_, p_115308_.xRotO, p_115308_.getXRot());
            if (isEntityUpsideDown(p_115308_)) {
                f6 *= -1.0F;
                f2 *= -1.0F;
            }
            if (p_115308_.hasPose(Pose.SLEEPING)) {
                Direction direction = p_115308_.getBedOrientation();
                if (direction != null) {
                    float f4 = p_115308_.getEyeHeight(Pose.STANDING) - 0.1F;
                    p_115311_.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
                }
            }
            float f7 = this.getBob(p_115308_, p_115310_);
            this.setupRotations(p_115308_, p_115311_, f7, f, p_115310_, p_115308_.getScale());
            p_115311_.scale(-1.0F, -1.0F, 1.0F);
            this.scale(p_115308_, p_115311_, p_115310_);
            p_115311_.translate(0.0F, -1.501F, 0.0F);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && p_115308_.isAlive()) {
                f8 = p_115308_.walkAnimation.speed(p_115310_);
                f5 = p_115308_.walkAnimation.position(p_115310_);
                if (p_115308_.isBaby()) {
                    f5 *= 3.0F;
                }

                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }

            this.model.prepareMobModel(p_115308_, f5, f8, p_115310_);
            this.model.setupAnim(p_115308_, f5, f8, f7, f2, f6);
            float alpha = 1.0F - ((float) p_115308_.becomeInvisible / p_115308_.invisibleTime());
            VertexConsumer vertexconsumer = p_115312_.getBuffer(RenderType.itemEntityTranslucentCull(this.getTextureLocation(p_115308_)));
            int i = getOverlayCoords(p_115308_, this.getWhiteOverlayProgress(p_115308_, p_115310_));
            this.model.renderToBuffer(p_115311_, vertexconsumer, p_115313_, i, net.minecraft.util.FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
            if (p_115308_.isCurrentlyGlowing()) {
                VertexConsumer vertexconsumer2 = p_115312_.getBuffer(RenderType.outline(this.getTextureLocation(p_115308_)));
                this.model.renderToBuffer(p_115311_, vertexconsumer2, p_115313_, i, 0xFFFFFFFF);
            }
            p_115311_.popPose();
        } else {
            super.render(p_115308_, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
        }
    }

    @Nullable
    protected RenderType getRenderType(T p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        ResourceLocation resourcelocation = this.getTextureLocation(p_115322_);
        if (p_115324_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_115323_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_115325_ ? RenderType.outline(resourcelocation) : RenderType.itemEntityTranslucentCull(resourcelocation);
        }
    }

    @Override
    protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) {
        p_115315_.scale(1.1F, 1.1F, 1.1F);
    }

    public ResourceLocation getTextureLocation(T p_116009_) {
        return TEXTURE;
    }

    public static class GlowLayer<T extends LivingEntity> extends EyesLayer<T, ProwlerModel<T>> {
        private static final RenderType GLOW = RenderType.eyes(ClangingHowl.location("textures/entity/prowler_e.png"));

        public GlowLayer(RenderLayerParent<T, ProwlerModel<T>> p_116964_) {
            super(p_116964_);
        }

        @Override
        public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
            if (!p_116986_.isInvisible()) {
                super.render(p_116983_, p_116984_, p_116985_, p_116986_, p_116987_, p_116988_, p_116989_, p_116990_, p_116991_, p_116992_);
            }
        }

        public RenderType renderType() {
            return GLOW;
        }
    }

    public static class OutlineLayer<T extends LivingEntity> extends EyesLayer<T, ProwlerModel<T>> {
        private static final RenderType OUTLINE = RenderType.eyes(ClangingHowl.location("textures/entity/prowler_outline.png"));

        public OutlineLayer(RenderLayerParent<T, ProwlerModel<T>> p_116964_) {
            super(p_116964_);
        }

        public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
            if (!p_116986_.isInvisible()) {
                VertexConsumer vertexconsumer = p_116984_.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer(p_116983_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 0x26FFFFFF);
            }
        }

        @Override
        public RenderType renderType() {
            return OUTLINE;
        }
    }
}
