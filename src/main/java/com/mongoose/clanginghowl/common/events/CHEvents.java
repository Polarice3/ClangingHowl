package com.mongoose.clanginghowl.common.events;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.capabilities.CHCapProvider;
import com.mongoose.clanginghowl.common.capabilities.ICHCap;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.CHTiers;
import com.mongoose.clanginghowl.common.items.energy.EnergyItem;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CHEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player original = event.getOriginal();

        original.reviveCaps();

        ICHCap capability3 = CHCapHelper.getCapability(original);
        player.getCapability(CHCapProvider.CAPABILITY)
                .ifPresent(cap ->
                        cap.setMiningProgress(0));
        player.getCapability(CHCapProvider.CAPABILITY)
                .ifPresent(cap ->
                        cap.setMiningPos(null));
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (event.getAmount() > 0.0F) {
            if (directEntity instanceof LivingEntity livingAttacker) {
                if (CHDamageSource.physicalAttacks(event.getSource())) {
                    if (livingAttacker.getMainHandItem().getItem() instanceof TieredItem weapon) {
                        if (weapon.getTier() == CHTiers.EXTRATERRESTRIAL) {
                            victim.addEffect(new MobEffectInstance(CHEffects.COSMIC_IRRADIATION.get(), 400));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player = event.player;
        Level world = player.level();
        if (event.phase == TickEvent.Phase.END) {
            if (!player.isUsingItem() || !(player.getUseItem().getItem() instanceof EnergyItem)) {
                EnergyItem.resetMiningProgress(world, player);
            }
        }
    }

    @SubscribeEvent
    public static void FurnaceBurnItems(FurnaceFuelBurnTimeEvent event){
        if (!event.getItemStack().isEmpty()){
            ItemStack itemStack = event.getItemStack();
            if (itemStack.is(CHItems.BLAZE_FUEL.get())) {
                event.setBurnTime(3200);
            }
        }
    }
}
