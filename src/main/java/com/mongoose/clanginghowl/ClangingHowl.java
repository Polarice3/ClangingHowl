package com.mongoose.clanginghowl;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mongoose.clanginghowl.client.inventory.menu.CHMenuTypes;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.CommonProxy;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.crafting.CHRecipeSerializers;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.hostiles.*;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.CHPotions;
import com.mongoose.clanginghowl.common.world.CHMobSpawnBiomeModifier;
import com.mongoose.clanginghowl.compat.CHCompat;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.*;
import com.mongoose.clanginghowl.mixin.FireBlockAccessor;
import com.mongoose.clanginghowl.utils.CHPotionUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(ClangingHowl.MOD_ID)
public class ClangingHowl {
    public static final String MOD_ID = "clanginghowl";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static CHProxy PROXY = new CommonProxy();

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public ClangingHowl(IEventBus modEventBus, ModContainer modContainer) {

        CHBlockEntities.BLOCK_ENTITY.register(modEventBus);
        CHEntityType.ENTITY_TYPE.register(modEventBus);
        CHParticleTypes.PARTICLE_TYPES.register(modEventBus);
        CHMenuTypes.MENU_TYPE.register(modEventBus);
        CHCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupEntityAttributeCreation);
        modEventBus.addListener(this::SpawnPlacementEvent);

        modContainer.registerConfig(ModConfig.Type.COMMON, CHConfig.SPEC, "clanginghowl.toml");

        final DeferredRegister<MapCodec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ClangingHowl.MOD_ID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("mob_spawns", CHMobSpawnBiomeModifier::makeCodec);

        NeoForge.EVENT_BUS.addListener(this::addBrewingRecipes);
        CHItems.init(modEventBus);
        CHBlocks.init(modEventBus);
        CHRecipeSerializers.init(modEventBus);
        CHEffects.init(modEventBus);
        CHPotions.init(modEventBus);
        CHSounds.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CHCompat.setup(event);
        event.enqueueWork(() -> {
            FireBlockAccessor fireBlockAccessor = (FireBlockAccessor) Blocks.FIRE;
            fireBlockAccessor.callSetFlammable(CHBlocks.BLAZE_FUEL_CYLINDER_BLOCK.get(), 15, 100);
            fireBlockAccessor.callSetFlammable(CHBlocks.TECHNOFLESH_BLOCK.get(), 5, 20);
            fireBlockAccessor.callSetFlammable(CHBlocks.TECHNOFLESH_SLAB.get(), 5, 20);
            fireBlockAccessor.callSetFlammable(CHBlocks.TECHNOFLESH_MEMBRANE.get(), 5, 20);
            fireBlockAccessor.callSetFlammable(CHBlocks.TECHNOFLESH_NEST.get(), 5, 20);
        });
    }

    private void addBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new CHPotionUtil(CHPotionUtil.setPotion(Potions.AWKWARD), Ingredient.of(CHItems.HEMATOMA_LUMP.get()), CHPotionUtil.setPotion(CHPotions.ATTRACTION)));
        event.getBuilder().addRecipe(new CHPotionUtil(CHPotionUtil.setSplashPotion(Potions.AWKWARD), Ingredient.of(CHItems.HEMATOMA_LUMP.get()), CHPotionUtil.setSplashPotion(CHPotions.ATTRACTION)));
        event.getBuilder().addRecipe(new CHPotionUtil(CHPotionUtil.setSplashPotion(Potions.AWKWARD), Ingredient.of(CHItems.HEMATOMA_LUMP.get()), CHPotionUtil.setLingeringPotion(CHPotions.ATTRACTION)));
        event.getBuilder().addRecipe(new CHPotionUtil(CHPotionUtil.setPotion(CHPotions.ATTRACTION), Ingredient.of(Items.REDSTONE), CHPotionUtil.setPotion(CHPotions.LONG_ATTRACTION)));
        event.getBuilder().addRecipe(new CHPotionUtil(CHPotionUtil.setSplashPotion(CHPotions.ATTRACTION), Ingredient.of(Items.REDSTONE), CHPotionUtil.setSplashPotion(CHPotions.LONG_ATTRACTION)));
        event.getBuilder().addRecipe(new CHPotionUtil(CHPotionUtil.setLingeringPotion(CHPotions.ATTRACTION), Ingredient.of(Items.REDSTONE), CHPotionUtil.setLingeringPotion(CHPotions.LONG_ATTRACTION)));
    }

    private void setupEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(CHEntityType.HEART_OF_DECAY.get(), HeartOfDecay.createAttributes().build());
        event.put(CHEntityType.EX_REAPER.get(), ExReaper.createAttributes().build());
        event.put(CHEntityType.FLESH_MAIDEN.get(), FleshMaiden.createAttributes().build());
        event.put(CHEntityType.HEMATOMA.get(), Hematoma.createAttributes().build());
        event.put(CHEntityType.BLOOD_SPREADER.get(), BloodSpreader.createAttributes().build());
        event.put(CHEntityType.BLOODY_COPY.get(), BloodyCopy.createAttributes().build());
        event.put(CHEntityType.BLOOD_CLOT.get(), BloodClot.createAttributes().build());
        event.put(CHEntityType.PROWLER.get(), Prowler.createAttributes().build());
        event.put(CHEntityType.CARCASS.get(), Carcass.createAttributes().build());
    }

    private void SpawnPlacementEvent(RegisterSpawnPlacementsEvent event){
        event.register(CHEntityType.HEART_OF_DECAY.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HeartOfDecay::checkHoDSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CHEntityType.EX_REAPER.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ExReaper::checkExReaperSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CHEntityType.FLESH_MAIDEN.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FleshMaiden::checkFleshMaidenSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CHEntityType.HEMATOMA.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hematoma::checkHematomaSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CHEntityType.BLOOD_SPREADER.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloodSpreader::checkBloodSpreaderSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CHEntityType.PROWLER.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Prowler::checkProwlerSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CHEntityType.CARCASS.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Carcass::checkCarcassSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }


}
