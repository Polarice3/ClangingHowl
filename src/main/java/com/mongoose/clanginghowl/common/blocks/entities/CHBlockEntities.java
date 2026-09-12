package com.mongoose.clanginghowl.common.blocks.entities;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ClangingHowl.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystalFormerBlockEntity>> CRYSTAL_FORMER = BLOCK_ENTITY.register("crystal_former",
            () -> BlockEntityType.Builder.of(CrystalFormerBlockEntity::new, CHBlocks.CRYSTAL_FORMER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChargingStationBlockEntity>> STATIONARY_CHARGING_STATION = BLOCK_ENTITY.register("stationary_charging_station",
            () -> BlockEntityType.Builder.of(ChargingStationBlockEntity::new, CHBlocks.STATIONARY_CHARGING_STATION.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExBarrierBlockEntity>> BARRIER_OF_EXTRATERRESTRIAL_ACTIVITY = BLOCK_ENTITY.register("barrier_of_extraterrestrial_activity",
            () -> BlockEntityType.Builder.of(ExBarrierBlockEntity::new, CHBlocks.BARRIER_OF_EXTRATERRESTRIAL_ACTIVITY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrokenCrystalFormerBlockEntity>> BROKEN_CRYSTAL_FORMER = BLOCK_ENTITY.register("broken_crystal_former",
            () -> BlockEntityType.Builder.of(BrokenCrystalFormerBlockEntity::new, CHBlocks.BROKEN_CRYSTAL_FORMER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FlameSpewerBlockEntity>> FLAME_SPEWER = BLOCK_ENTITY.register("flame_spewer",
            () -> BlockEntityType.Builder.of(FlameSpewerBlockEntity::new, CHBlocks.FLAME_SPEWER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrokenSteelLampBlockEntity>> BROKEN_STEEL_LAMP = BLOCK_ENTITY.register("broken_steel_lamp",
            () -> BlockEntityType.Builder.of(BrokenSteelLampBlockEntity::new, CHBlocks.BROKEN_STEEL_LAMP.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MotionSensorBlockEntity>> MOTION_SENSOR = BLOCK_ENTITY.register("motion_sensor",
            () -> BlockEntityType.Builder.of(MotionSensorBlockEntity::new, CHBlocks.MOTION_SENSOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExActivityRadarBlockEntity>> EXTRATERRESTRIAL_ACTIVITY_RADAR = BLOCK_ENTITY.register("extraterrestrial_activity_radar",
            () -> BlockEntityType.Builder.of(ExActivityRadarBlockEntity::new, CHBlocks.EXTRATERRESTRIAL_ACTIVITY_RADAR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NerveEndingsBlockEntity>> NERVE_ENDINGS = BLOCK_ENTITY.register("nerve_endings",
            () -> BlockEntityType.Builder.of(NerveEndingsBlockEntity::new, CHBlocks.NERVE_ENDINGS.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FleshNestBlockEntity>> TECHNOFLESH_NEST = BLOCK_ENTITY.register("technoflesh_nest",
            () -> BlockEntityType.Builder.of(FleshNestBlockEntity::new, CHBlocks.TECHNOFLESH_NEST.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ConsummateNestBlockEntity>> CONSUMMATE_NEST = BLOCK_ENTITY.register("consummate_nest",
            () -> BlockEntityType.Builder.of(ConsummateNestBlockEntity::new, CHBlocks.CONSUMMATE_NEST.get()).build(null));

}
