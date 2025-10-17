package com.mongoose.clanginghowl.common.entities.projectiles;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.utils.MathHelper;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class SpitProjectile extends ThrowableProjectile {

    public SpitProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public SpitProjectile(double p_37457_, double p_37458_, double p_37459_, Level p_37460_) {
        super(CHEntityType.SPIT_PROJECTILE.get(), p_37457_, p_37458_, p_37459_, p_37460_);
    }

    public SpitProjectile(LivingEntity p_37463_, Level p_37464_) {
        super(CHEntityType.SPIT_PROJECTILE.get(), p_37463_, p_37464_);
    }

    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() - (vec3.x * 1.5D);
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - (vec3.z * 1.5D);
        Vec3 vec31 = new Vec3(d0, d1, d2);
        this.level().addParticle(CHParticleTypes.SPIT.get(), vec31.x, vec31.y + 0.375D, vec31.z, 0.0D, 0.0D, 0.0D);
        if (this.tickCount >= MathHelper.secondsToTicks(10)){
            this.discard();
        }
    }

    protected void onHitEntity(EntityHitResult p_37626_) {
        super.onHitEntity(p_37626_);
        if (!this.level().isClientSide) {
            float baseDamage = 3.0F;
            Entity target = p_37626_.getEntity();
            Entity entity1 = this.getOwner();
            boolean flag;
            if (entity1 instanceof LivingEntity livingentity) {
                if (livingentity instanceof Mob mob){
                    if (mob.getAttribute(Attributes.ATTACK_DAMAGE) != null && mob.getAttributeValue(Attributes.ATTACK_DAMAGE) > 0){
                        baseDamage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    }
                }
                flag = target.hurt(target.damageSources().mobProjectile(this, livingentity), baseDamage);
                if (flag) {
                    if (target.isAlive()) {
                        this.doEnchantDamageEffects(livingentity, target);
                    }
                }
            } else {
                flag = target.hurt(target.damageSources().magic(), baseDamage);
            }
            if (flag) {
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(CHEffects.NEUROTOXIN.get(), 160));
                }
            }

        }
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if (MobUtil.areAllies(this.getOwner(), pEntity)){
                    return false;
                }
                if (this.getOwner() instanceof Enemy && pEntity instanceof Enemy){
                    return false;
                }
            }
        }
        return super.canHitEntity(pEntity);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void makeStuckInBlock(BlockState p_33796_, Vec3 p_33797_) {
        if (!p_33796_.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(p_33796_, p_33797_);
        }

    }

    protected float getGravity() {
        return 0.01F;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
