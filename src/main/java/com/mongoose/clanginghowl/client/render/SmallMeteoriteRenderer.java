package com.mongoose.clanginghowl.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.render.model.SmallMeteoriteModel;
import com.mongoose.clanginghowl.client.render.visual.TrailRenderer;
import com.mongoose.clanginghowl.common.entities.projectiles.SmallMeteorite;
import com.mongoose.clanginghowl.utils.ColorUtil;
import com.mongoose.clanginghowl.utils.TrailEffect;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class SmallMeteoriteRenderer extends EntityRenderer<SmallMeteorite> {
   private static final ResourceLocation METEORITE = ClangingHowl.location("textures/entity/projectiles/small_meteorite.png");
   private static final ResourceLocation GLOW = ClangingHowl.location("textures/entity/projectiles/small_meteorite_glow.png");
   private static final ResourceLocation OUTLINE = ClangingHowl.location("textures/entity/projectiles/small_meteorite_outline.png");
   private static final ResourceLocation TRAIL_TEXTURE = ClangingHowl.location("textures/entity/projectiles/solid_trail.png");
   private final SmallMeteoriteModel<SmallMeteorite> model;
   private final RandomSource random = RandomSource.create();

   public SmallMeteoriteRenderer(EntityRendererProvider.Context p_174449_) {
      super(p_174449_);
      this.model = new SmallMeteoriteModel<>(p_174449_.bakeLayer(CHModelLayer.SMALL_METEORITE));
   }

   protected int getBlockLightLevel(SmallMeteorite p_116491_, BlockPos p_116492_) {
      return 15;
   }

   public void render(SmallMeteorite p_116484_, float p_116485_, float p_116486_, PoseStack p_116487_, MultiBufferSource p_116488_, int p_116489_) {
      p_116487_.pushPose();
      p_116487_.scale(-1.0F, -1.0F, 1.0F);
      p_116487_.translate(0, -1.501F, 0.0F);
      float f = Mth.rotLerp(p_116486_, p_116484_.yRotO, p_116484_.getYRot());
      float f1 = Mth.lerp(p_116486_, p_116484_.xRotO, p_116484_.getXRot());
      VertexConsumer vertexconsumer = p_116488_.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(p_116484_)));
      this.model.setupAnim(0.0F, f, f1);
      this.model.renderToBuffer(p_116487_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 0x80FFFFFF);
      VertexConsumer vertexconsumer2 = p_116488_.getBuffer(RenderType.eyes(GLOW));
      this.model.setupAnim(0.0F, f, f1);
      this.model.renderToBuffer(p_116487_, vertexconsumer2, 15728640, OverlayTexture.NO_OVERLAY, 0x80FFFFFF);
      VertexConsumer vertexconsumer3 = p_116488_.getBuffer(RenderType.eyes(OUTLINE));
      this.model.setupAnim(0.0F, f, f1);
      this.model.renderToBuffer(p_116487_, vertexconsumer3, 15728640, OverlayTexture.NO_OVERLAY, 0x26FFFFFF);
      p_116487_.popPose();

      ColorUtil colorUtil = new ColorUtil(0xffb52a);
      float r = colorUtil.red;
      float g = colorUtil.green;
      float b = colorUtil.blue;
      p_116487_.pushPose();
      float x = (float) (Mth.lerp(p_116486_, p_116484_.xOld, p_116484_.getX()));
      float y = (float) (Mth.lerp(p_116486_, p_116484_.yOld, p_116484_.getY()));
      float z = (float) (Mth.lerp(p_116486_, p_116484_.zOld, p_116484_.getZ()));
      p_116484_.trail.prepareRender(new Vec3(x, y + p_116484_.getBbHeight() / 1.5F, z), p_116486_);
      p_116487_.translate(-x, -y, -z);
      TrailRenderer.render(p_116484_.trail, p_116488_.getBuffer(RenderType.entityCutoutNoCull(TRAIL_TEXTURE)), p_116487_, TrailEffect.TrailOffsetFunction.FACE_CAMERA, true, r, g, b, 1, LightTexture.FULL_BRIGHT);
      p_116487_.popPose();

      super.render(p_116484_, p_116485_, p_116486_, p_116487_, p_116488_, p_116489_);
   }

   public ResourceLocation getTextureLocation(SmallMeteorite p_116482_) {
      return METEORITE;
   }
}