package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.blocks.ExEnergyClusterBlock;
import com.mongoose.clanginghowl.common.blocks.HugeExEnergyClusterBlock;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Based on @klikli-dev's Block Loot Generator
 */
public class CHBlockLootProvider extends BlockLootSubProvider {
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
    private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
    private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();
    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

    public CHBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        this.generate();
        this.map.forEach(consumer::accept);
    }

    @Override
    protected void generate() {
        Collection<Block> blocks = new ArrayList<>();
        CHBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block ->
        {
            CHBlocks.BlockLootSetting setting = CHBlocks.BLOCK_LOOT.get(ForgeRegistries.BLOCKS.getKey(block));
            if (setting.lootTableType == CHBlocks.LootTableType.DROP){
                blocks.add(block);
            }
        });
        for (Block block : blocks){
            if (block instanceof DoorBlock){
                this.add(block, createDoorTable(block));
            } else if (block instanceof SlabBlock){
                this.add(block, createSlabItemTable(block));
            } else {
                this.dropSelf(block);
            }
        }
        LootItemCondition.Builder lootitemcondition$builder0 =
                LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(ExEnergyClusterBlock.AGE, 0));
        LootItemCondition.Builder lootitemcondition$builder =
                LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(ExEnergyClusterBlock.AGE, 1));
        LootItemCondition.Builder lootitemcondition$builder2 =
                LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(ExEnergyClusterBlock.AGE, 2));
        this.add(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get(),
                this.applyExplosionDecay(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH)
                                        .when(lootitemcondition$builder0)
                                        .add(LootItem.lootTableItem(CHBlocks.SMALL_EX_ENERGY_CLUSTER.get())))
                                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH)
                                        .when(lootitemcondition$builder)
                                        .add(LootItem.lootTableItem(CHBlocks.MEDIUM_EX_ENERGY_CLUSTER.get())))
                                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH)
                                        .when(lootitemcondition$builder2)
                                        .add(LootItem.lootTableItem(CHBlocks.LARGE_EX_ENERGY_CLUSTER.get())))
                                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH.invert())
                                        .add(LootItem.lootTableItem(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())))
                                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH.invert())
                                        .when(lootitemcondition$builder)
                                        .add(LootItem.lootTableItem(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())))
                                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH.invert())
                                        .when(lootitemcondition$builder2)
                                        .add(LootItem.lootTableItem(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())))));
        this.add(CHBlocks.HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER.get(), this.createHugeExEnergyCluster());
        this.add(CHBlocks.METEORITE_STEEL_ORE.get(), (p_124076_) -> {
            return createOreDrop(p_124076_, CHItems.EXTRATERRESTRIAL_STEEL.get(), 1, 3);
        });
        this.add(CHBlocks.EXTRATERRESTRIAL_STEEL_ORE.get(), (p_124076_) -> {
            return createOreDrop(p_124076_, CHItems.EXTRATERRESTRIAL_STEEL.get());
        });
        this.add(CHBlocks.BIG_CRYOGENIC_ICICLE.get(), this.createBigIcicle());
        this.add(CHBlocks.TECHNOFLESH_MEMBRANE.get(), this.dropSilkTouchOrShears(CHBlocks.TECHNOFLESH_MEMBRANE.get()));
        this.add(CHBlocks.HANGING_TECHNOFLESH.get(), this.dropSilkTouchOrShears(CHBlocks.HANGING_TECHNOFLESH.get()));
        this.add(CHBlocks.BIG_HANGING_TECHNOFLESH.get(), this.createBigHangingFlesh());
        this.dropWhenSilkTouch(CHBlocks.NERVE_ENDINGS.get());
        this.dropWhenSilkTouch(CHBlocks.TECHNOFLESH_NEST.get());
    }

    protected LootTable.Builder createHugeExEnergyCluster() {
        Block block = CHBlocks.HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER.get();
        Item item = CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get();
        LootItemCondition.Builder lootitemcondition$builder3 =
                LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(HugeExEnergyClusterBlock.HALF, DoubleBlockHalf.LOWER));
        return createSilkTouchDispatchTable(block,
                applyExplosionDecay(block, LootItem.lootTableItem(item)
                        .when(lootitemcondition$builder3)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))));
    }

    protected LootTable.Builder createBigHangingFlesh() {
        Block block = CHBlocks.BIG_HANGING_TECHNOFLESH.get();
        Item item = CHBlocks.HANGING_TECHNOFLESH.get().asItem();
        LootItemCondition.Builder lootitemcondition$builder3 =
                LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(HugeExEnergyClusterBlock.HALF, DoubleBlockHalf.UPPER));
        return dropSilkTouchOrShears(item, 2, lootitemcondition$builder3);
    }

    protected LootTable.Builder createBigIcicle() {
        Block block = CHBlocks.BIG_CRYOGENIC_ICICLE.get();
        Item item = CHBlocks.CRYOGENIC_ICICLE.get().asItem();
        LootItemCondition.Builder lootitemcondition$builder3 =
                LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(HugeExEnergyClusterBlock.HALF, DoubleBlockHalf.UPPER));
        return LootTable.lootTable().withPool(this.applyExplosionCondition(item, LootPool.lootPool().setRolls(ConstantValue.exactly(2)).add(LootItem.lootTableItem(item)).when(lootitemcondition$builder3)));
    }

    public LootTable.Builder dropSilkTouchOrShears(ItemLike p_251912_, int count, LootItemCondition.Builder other) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(p_251912_, LootPool.lootPool().setRolls(ConstantValue.exactly(count)).add(LootItem.lootTableItem(p_251912_))).when(HAS_SHEARS_OR_SILK_TOUCH.and(other)));
    }

    protected LootTable.Builder dropSilkTouchOrShears(ItemLike p_251912_) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(p_251912_, LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(p_251912_))).when(HAS_SHEARS_OR_SILK_TOUCH));
    }

    public LootTable.Builder createConstantDrop(ItemLike p_251912_, int count) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(p_251912_, LootPool.lootPool().setRolls(ConstantValue.exactly(count)).add(LootItem.lootTableItem(p_251912_))));
    }

    protected LootTable.Builder createConstantDrop(Block p_124140_, Item p_124141_, int count) {
        return createSilkTouchDispatchTable(p_124140_, applyExplosionDecay(p_124140_, LootItem.lootTableItem(p_124141_).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))));
    }

    protected LootTable.Builder createOreDrop(Block p_124140_, Item p_124141_, int min, int max) {
        return createSilkTouchDispatchTable(p_124140_, applyExplosionDecay(p_124140_, LootItem.lootTableItem(p_124141_).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createOreDrop(Block p_124140_, Item p_124141_) {
        return createSilkTouchDispatchTable(p_124140_, applyExplosionDecay(p_124140_, LootItem.lootTableItem(p_124141_).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
}
