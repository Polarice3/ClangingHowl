package com.mongoose.clanginghowl.smoke;

import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.capabilities.CHAttachments;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.common.world.data.ICHWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import java.util.ArrayList;
import java.util.List;

/** Development-only tests, excluded from the published mod JAR. */
@GameTestHolder("clanginghowl")
@PrefixGameTestTemplate(false)
public final class PortGameTests {
    @GameTest(template = "port_smoke", timeoutTicks = 100)
    public static void allLivingEntitiesTickAndPersist(GameTestHelper helper) {
        var types = List.of(CHEntityType.HEART_OF_DECAY, CHEntityType.EX_REAPER,
                CHEntityType.FLESH_MAIDEN, CHEntityType.HEMATOMA, CHEntityType.BLOOD_SPREADER,
                CHEntityType.BLOODY_COPY, CHEntityType.BLOOD_CLOT, CHEntityType.PROWLER, CHEntityType.CARCASS);
        List<Mob> mobs = new ArrayList<>();
        int index = 0;
        for (var type : types) {
            Mob mob = helper.spawn(type.get(), 1 + index % 3 * 3, 2, 1 + index / 3 * 3);
            mob.setNoAi(true);
            mob.setInvulnerable(true);
            mob.getData(CHAttachments.STATE).setTechnoResist(12);
            mobs.add(mob);
            index++;
        }
        helper.runAfterDelay(20, () -> {
            for (Mob mob : mobs) {
                helper.assertTrue(mob.isAlive(), "Entity did not survive tick: " + mob.getType());
                var saved = new net.minecraft.nbt.CompoundTag();
                helper.assertTrue(mob.save(saved), "Entity save failed");
                var copy = mob.getType().create(helper.getLevel());
                helper.assertTrue(copy != null, "Entity recreation failed");
                copy.load(saved);
                helper.assertTrue(copy.getData(CHAttachments.STATE).technoResist() == 12,
                        "Attachment did not survive entity save/load");
            }
            helper.succeed();
        });
    }

    @GameTest(template = "port_smoke")
    public static void neurotoxinUsesServerMobMovement(GameTestHelper helper) {
        var moving = helper.spawn(net.minecraft.world.entity.EntityType.COW, 2, 1, 2);
        var stationary = helper.spawn(net.minecraft.world.entity.EntityType.COW, 6, 1, 6);
        moving.setNoAi(true);
        stationary.setNoAi(true);
        moving.tickCount = stationary.tickCount = 20;
        moving.setDeltaMovement(0.1, 0, 0);
        stationary.setDeltaMovement(0, 0, 0);
        float movingHealth = moving.getHealth();
        float stationaryHealth = stationary.getHealth();
        var neurotoxin = com.mongoose.clanginghowl.common.effects.CHEffects.NEUROTOXIN.get();
        neurotoxin.applyEffectTick(moving, 0);
        neurotoxin.applyEffectTick(stationary, 0);
        helper.assertTrue(moving.getHealth() < movingHealth, "Moving mob escaped neurotoxin without any connected client");
        helper.assertTrue(stationary.getHealth() == stationaryHealth, "Stationary mob received movement-only damage");
        helper.succeed();
    }

    @GameTest(template = "port_smoke")
    public static void energyComponentsAreCopiedAndSaved(GameTestHelper helper) {
        ItemStack stack = new ItemStack(CHItems.ADVANCED_CHAINSAW.get());
        ((IEnergyItem) stack.getItem()).setTagTick(stack);
        IEnergyItem.setEnergy(stack, 80);
        ItemStack copy = stack.copy();
        IEnergyItem.decreaseEnergy(copy, 15);
        helper.assertTrue(IEnergyItem.currentEnergy(stack) == 80, "Copy mutated original energy");
        helper.assertTrue(IEnergyItem.currentEnergy(copy) == 65, "Energy consumption was lost");
        var registries = helper.getLevel().registryAccess();
        ItemStack loaded = ItemStack.parse(registries, copy.save(registries)).orElseThrow();
        helper.assertTrue(IEnergyItem.currentEnergy(loaded) == 65, "Item save/load lost energy");
        helper.succeed();
    }

    @GameTest(template = "port_smoke")
    public static void datapackRegistriesAndWorldDataLoad(GameTestHelper helper) {
        var level = helper.getLevel();
        var enchantments = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        for (var key : CHEnchantments.ALL) {
            helper.assertTrue(enchantments.getHolder(key).isPresent(), "Missing enchantment: " + key.location());
        }
        long recipes = level.getRecipeManager().getRecipeIds()
                .filter(id -> id.getNamespace().equals("clanginghowl")).count();
        helper.assertTrue(recipes > 50, "Mod recipes did not load: " + recipes);
        helper.assertTrue(((ICHWorldData) level).getCHWorldData() != null, "Meteor world-data mixin not initialized");
        helper.succeed();
    }

    @GameTest(template = "port_smoke", timeoutTicks = 100)
    public static void blockEntitiesTickAndRoundTrip(GameTestHelper helper) {
        var blocks = List.of(CHBlocks.CRYSTAL_FORMER, CHBlocks.STATIONARY_CHARGING_STATION,
                CHBlocks.BARRIER_OF_EXTRATERRESTRIAL_ACTIVITY, CHBlocks.BROKEN_CRYSTAL_FORMER,
                CHBlocks.FLAME_SPEWER, CHBlocks.BROKEN_STEEL_LAMP, CHBlocks.MOTION_SENSOR,
                CHBlocks.EXTRATERRESTRIAL_ACTIVITY_RADAR, CHBlocks.NERVE_ENDINGS,
                CHBlocks.TECHNOFLESH_NEST, CHBlocks.CONSUMMATE_NEST);
        var entities = new ArrayList<BlockEntity>();
        int i = 0;
        for (var block : blocks) {
            BlockPos pos = new BlockPos(1 + i % 4 * 2, 1, 1 + i / 4 * 3);
            helper.setBlock(pos, block.get());
            BlockEntity entity = helper.getBlockEntity(pos);
            helper.assertTrue(entity != null, "Missing block entity: " + block.getId());
            entities.add(entity);
            i++;
        }
        helper.runAfterDelay(10, () -> {
            var registries = helper.getLevel().registryAccess();
            for (BlockEntity entity : entities) {
                var tag = entity.saveWithFullMetadata(registries);
                var copy = BlockEntity.loadStatic(entity.getBlockPos(), entity.getBlockState(), tag, registries);
                helper.assertTrue(copy != null && copy.getType() == entity.getType(), "Block entity round-trip failed");
            }
            helper.succeed();
        });
    }
}
