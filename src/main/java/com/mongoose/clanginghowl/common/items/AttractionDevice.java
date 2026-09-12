package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.ItemHelper;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class AttractionDevice extends Item {
    public AttractionDevice() {
        super(new Properties().rarity(Rarity.UNCOMMON).durability(10));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!player.getCooldowns().isOnCooldown(this)) {
            player.swing(hand);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), CHSounds.SMOKE_RELEASE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            if (player.level() instanceof ServerLevel serverLevel) {
                ParticleUtil.attractionCloud(serverLevel, player);
            }
            player.addEffect(new MobEffectInstance(CHEffects.ATTRACTION, 500, 0));
            player.getCooldowns().addCooldown(this, 200);
            ItemHelper.hurtAndBreak(itemStack, 1, player);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.item.Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("info.clanginghowl.item.attraction_device"));
    }
}
