package com.mongoose.clanginghowl.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mongoose.clanginghowl.client.render.model.EnergyGloveModel;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.curios.XRayGoggles;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import javax.annotation.Nullable;

public record WearRenderer(ResourceLocation texture,
                           HumanoidModel<LivingEntity> model) implements ICurioRenderer {

    private ResourceLocation getTexture(LivingEntity livingEntity, ItemStack stack) {
        if (stack.is(CHItems.X_RAY_GOGGLES.get())) {
            if (XRayGoggles.isActivated(stack)) {
                return CuriosRenderer.render("x_ray_goggles.png");
            }
        }
        if (stack.is(CHItems.ENERGY_GLOVE.get())) {
            if (!IEnergyItem.isEmpty(stack)) {
                return CuriosRenderer.render("energy_glove.png");
            }
        }
        return texture;
    }

    private HumanoidModel<LivingEntity> getModel() {
        return model;
    }

    @Nullable
    public static WearRenderer getRenderer(ItemStack stack) {
        if (!stack.isEmpty()) {
            return (WearRenderer) CuriosRendererRegistry.getRenderer(stack.getItem()).orElse(null);
        }
        return null;
    }

    public boolean hasCape(AbstractClientPlayer p_116618_){
        return p_116618_.getSkin().capeTexture() != null && !p_116618_.isInvisible() && p_116618_.isModelPartShown(PlayerModelPart.CAPE) && p_116618_.getSkin().capeTexture() != null;
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity livingEntity = slotContext.entity();
        HumanoidModel<LivingEntity> model = this.getModel();

        model.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
        ICurioRenderer.followBodyRotations(slotContext.entity(), model);
        render(livingEntity, stack, matrixStack, renderTypeBuffer, light);
    }

    private void render(LivingEntity livingEntity, ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        RenderType renderType = this.getModel().renderType(getTexture(livingEntity, stack));
        VertexConsumer vertexBuilder = buffer.getBuffer(renderType);
        this.getModel().renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        if (stack.is(CHItems.X_RAY_GOGGLES.get())) {
            if (XRayGoggles.isActivated(stack)) {
                VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("x_ray_goggles_emissive.png")));
                this.getModel().renderToBuffer(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }
        if (stack.is(CHItems.ENERGY_BARRIER_GENERATOR.get())) {
            if (!IEnergyItem.isEmpty(stack)) {
                VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("energy_barrier_generator_emissive.png")));
                this.getModel().renderToBuffer(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }
        if (stack.is(CHItems.TENDON_STRENGTHENER.get())) {
            if (!IEnergyItem.isEmpty(stack)) {
                VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("tendon_strengthener_emissive.png")));
                this.getModel().renderToBuffer(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }
        if (stack.is(CHItems.ENERGY_GLOVE.get())) {
            if (!IEnergyItem.isEmpty(stack)) {
                VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("energy_glove_emissive.png")));
                this.getModel().renderToBuffer(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0x26FFFFFF);
            }
        }
        if (stack.is(CHItems.BLOODY_BATTERY.get())) {
            VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("bloody_battery_emissive.png")));
            this.getModel().renderToBuffer(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0x26FFFFFF);
        }
        if (stack.is(CHItems.REANIMATOR.get())) {
            if (IEnergyItem.isFull(stack)) {
                VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("reanimator_emissive.png")));
                this.getModel().renderToBuffer(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }
    }

    public void renderFirstPersonArm(PoseStack matrixStack, ItemStack itemStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, boolean hasFoil) {
        if (!player.isSpectator()) {
            EnergyGloveModel model = (EnergyGloveModel) getModel();

            ModelPart arm = model.rightArm;

            model.setAllVisible(false);
            arm.visible = true;

            model.crouching = false;
            model.attackTime = 0;
            model.swimAmount = 0;
            model.setupAnim(player, 0, 0, 0, 0, 0);
            arm.xRot = 0;

            renderFirstPersonArm(player, itemStack, model, arm, matrixStack, buffer, light, hasFoil);
        }
    }

    private void renderFirstPersonArm(LivingEntity livingEntity, ItemStack itemStack, EnergyGloveModel model, ModelPart arm, PoseStack matrixStack, MultiBufferSource buffer, int light, boolean hasFoil) {
        RenderType renderType = model.renderType(getTexture(livingEntity, itemStack));
        VertexConsumer builder = ItemRenderer.getFoilBuffer(buffer, renderType, false, hasFoil);
        arm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
        if (!IEnergyItem.isEmpty(itemStack)) {
            VertexConsumer vertexBuilder2 = buffer.getBuffer(RenderType.eyes(CuriosRenderer.render("energy_glove_emissive.png")));
            arm.render(matrixStack, vertexBuilder2, light, OverlayTexture.NO_OVERLAY, 0x26FFFFFF);
        }
    }
}
