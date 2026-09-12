package com.mongoose.clanginghowl.common.items.curios;

import com.mongoose.clanginghowl.utils.CHItemData;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.init.CHKeybindings;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import com.mongoose.clanginghowl.utils.CHUUIDUtil;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class XRayGoggles extends CuriosEnergyItem implements IActivatable {
    private static final String ACTIVATED = "Activated";

    public XRayGoggles() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void activate(Level level, Player player, ItemStack itemStack) {
        if (itemStack.is(this)) {
            if (!player.getCooldowns().isOnCooldown(this)) {
                if (!isActivated(itemStack)) {
                    if (IEnergyItem.currentEnergy(itemStack) >= 6) {
                        setActivated(itemStack, true);
                        IEnergyItem.decreaseEnergy(itemStack, 6);
                        level.playSound(null, player, CHSounds.X_RAY_ACTIVATION.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        player.getCooldowns().addCooldown(this, 30);
                    } else {
                        player.displayClientMessage(Component.translatable("info.clanginghowl.tool.empty"), true);
                    }
                } else {
                    if (isActivated(itemStack)) {
                        setActivated(itemStack, false);
                        level.playSound(null, player, CHSounds.DISCHARGED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        player.getCooldowns().addCooldown(this, 30);
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity wearer && CHCuriosFinder.findCurio(wearer, this) == stack) {
            if (isActivated(stack)) {
                if (!IEnergyItem.isEmpty(stack)) {
                    if (entityIn.tickCount % 20 == 0) {
                        this.consumeEnergy(stack);
                    }
                    if (!worldIn.isClientSide) {
                        wearer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 0, false, false));
                        for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, entityIn.getBoundingBox().inflate(20.0D), livingEntity -> livingEntity != wearer && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity))) {
                            livingEntity.addEffect(new MobEffectInstance(CHEffects.ENLIGHTENED, 200, 0, false, false));
                        }
                    }
                } else {
                    if (!worldIn.isClientSide) {
                        if (this.isNightVision(wearer.getActiveEffects())) {
                            wearer.removeEffect(MobEffects.NIGHT_VISION);
                        }
                    }
                    worldIn.playSound(null, entityIn, CHSounds.DISCHARGED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    setActivated(stack, false);
                }
            }
        }
    }

    public boolean isNightVision(Collection<MobEffectInstance> activeEffects) {
        return !activeEffects.isEmpty() && activeEffects.stream().anyMatch(entity -> entity.getEffect() == MobEffects.NIGHT_VISION && entity.getDuration() <= 2);
    }

    public ItemStack getPoweredItem(){
        ItemStack itemStack = new ItemStack(this);
        IEnergyItem.setEnergy(itemStack, this.getMaxEnergy());
        IEnergyItem.setMaxEnergyAmount(itemStack, this.getMaxEnergy());
        setActivated(itemStack, true);
        return itemStack;
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        setActivated(pStack, true);
        super.onCraftedBy(pStack, pLevel, pPlayer);
    }

    public static void setActivated(ItemStack stack, boolean activated){
        if (CHItemData.hasData(stack)) {
            CHItemData.putBoolean(stack, ACTIVATED, activated);
        } else {

            CHItemData.putBoolean(stack, ACTIVATED, activated);
        }
    }

    public static boolean isActivated(ItemStack stack) {
        if (CHItemData.hasData(stack)) {
            return CHItemData.getBoolean(stack, ACTIVATED);
        } else {
            return false;
        }
    }

    @Override
    public int getMaxEnergy() {
        return 2000;
    }

    @Override
    public Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        net.minecraft.resources.ResourceLocation id, ItemStack stack) {
        Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(Attributes.ARMOR, new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("item.clanginghowl.x_ray_goggles.armor"), 1.0F, AttributeModifier.Operation.ADD_VALUE));
        return map;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addEnergyText(stack, tooltipContext, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        Component component = Component.literal("G");
        if (CHKeybindings.curioActivate() != null) {
            component = CHKeybindings.curioActivate().getTranslatedKeyMessage();
        }
        tooltip.add(Component.translatable("info.clanginghowl.item.x_ray.0", component));
        tooltip.add(Component.translatable("info.clanginghowl.item.x_ray.1"));
        tooltip.add(Component.translatable("info.clanginghowl.item.x_ray.2"));
    }
}
