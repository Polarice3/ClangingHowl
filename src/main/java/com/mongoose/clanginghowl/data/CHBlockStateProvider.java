package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CHBlockStateProvider extends BlockStateProvider {
    public CHBlockStateProvider(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, ClangingHowl.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE.get());
        simpleBlockWithItem(CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS.get());
        simpleBlockWithItem(CHBlocks.CARVED_EXTRATERRESTRIAL_STONE_BRICKS.get());
        simpleBlockWithItem(CHBlocks.RAW_EXTRATERRESTRIAL_STEEL_BLOCK.get());
        simpleBlockWithItem(CHBlocks.EXTRATERRESTRIAL_STEEL_BLOCK.get());
        simpleBlockWithItem(CHBlocks.STEEL_PLATE_BLOCK.get());
        simpleBlockWithItem(CHBlocks.DAMAGED_STEEL_PLATE_BLOCK.get());
        simpleBlockWithItem(CHBlocks.METEORITE_STEEL_ORE.get());
        simpleBlockWithItem(CHBlocks.EXTRATERRESTRIAL_STEEL_ORE.get());
        simpleBlockWithItem(CHBlocks.CALCITE_TILES.get());
        simpleBlockWithItem(CHBlocks.CRACKED_CALCITE_TILES.get());

        doorBlockWithRenderType((DoorBlock) CHBlocks.STEEL_DOOR.get(), ClangingHowl.location("block/steel_door_bottom"), ClangingHowl.location("block/steel_door_top"), "translucent");

        sideBottomTopColumnBlock((RotatedPillarBlock) CHBlocks.CARVED_STEEL_PLATE_BLOCK.get(), ClangingHowl.location("block/carved_steel_plate_block_top"), ClangingHowl.location("block/carved_steel_plate_block"), ClangingHowl.location("block/carved_steel_plate_block_bottom"));
        sideBottomTopColumnBlock((RotatedPillarBlock) CHBlocks.DAMAGED_CARVED_STEEL_PLATE_BLOCK.get(), ClangingHowl.location("block/damaged_carved_steel_plate_block_top"), ClangingHowl.location("block/damaged_carved_steel_plate_block"), ClangingHowl.location("block/damaged_carved_steel_plate_block_bottom"));

        columnBlockWithItem((RotatedPillarBlock) CHBlocks.EXTRATERRESTRIAL_COLUMN.get(), ClangingHowl.location("block/extraterrestrial_column"), ClangingHowl.location("block/extraterrestrial_column_top"));

        slabBlockWithItem((SlabBlock) CHBlocks.EXTRATERRESTRIAL_STONE_SLAB.get(), ClangingHowl.location("block/extraterrestrial_stone"));
        slabBlockWithItem((SlabBlock) CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_SLAB.get(), ClangingHowl.location("block/smooth_extraterrestrial_stone"));
        slabBlockWithItem((SlabBlock) CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB.get(), ClangingHowl.location("block/extraterrestrial_stone_bricks"));
        slabBlockWithItem((SlabBlock) CHBlocks.CARVED_STEEL_PLATE_SLAB.get(), ClangingHowl.location("block/carved_steel_plate_block"));
        slabBlockWithItem((SlabBlock) CHBlocks.CALCITE_TILE_SLAB.get(), ClangingHowl.location("block/calcite_tiles"));

        stairsBlockWithItem((StairBlock) CHBlocks.EXTRATERRESTRIAL_STONE_STAIRS.get(), ClangingHowl.location("block/extraterrestrial_stone"));
        stairsBlockWithItem((StairBlock) CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS.get(), ClangingHowl.location("block/smooth_extraterrestrial_stone"));
        stairsBlockWithItem((StairBlock) CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS.get(), ClangingHowl.location("block/extraterrestrial_stone_bricks"));
        stairsBlockWithItem((StairBlock) CHBlocks.CARVED_STEEL_PLATE_STAIRS.get(), ClangingHowl.location("block/carved_steel_plate_block"));
        stairsBlockWithItem((StairBlock) CHBlocks.CALCITE_TILE_STAIRS.get(), ClangingHowl.location("block/calcite_tiles"));

        wallBlockWithItem((WallBlock) CHBlocks.EXTRATERRESTRIAL_STONE_WALL.get(), ClangingHowl.location("block/extraterrestrial_stone"));
        wallBlockWithItem((WallBlock) CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_WALL.get(), ClangingHowl.location("block/smooth_extraterrestrial_stone"));
        wallBlockWithItem((WallBlock) CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL.get(), ClangingHowl.location("block/extraterrestrial_stone_bricks"));

        trapdoorBlockWithItem((TrapDoorBlock) CHBlocks.STEEL_TRAPDOOR.get(), ClangingHowl.location("block/steel_trapdoor"), true);
    }

    public ModelFile cubeAllWithRender(Block block, String renderType) {
        return models().cubeAll(name(block), blockTexture(block)).renderType(renderType);
    }

    public void simpleBlockWithItem(Block block) {
        simpleBlock(block, cubeAll(block));
        simpleBlockItem(block, cubeAll(block));
    }

    public void simpleBlockWithItemAndRender(Block block, String renderType) {
        simpleBlock(block, cubeAllWithRender(block, renderType));
        simpleBlockItem(block, cubeAllWithRender(block, renderType));
    }

    public void slabBlockWithItem(SlabBlock block, ResourceLocation texture){
        slabBlock(block, texture);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().slab(baseName, texture, texture, texture));
    }

    public void slabBlockWithItem(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation texture){
        slabBlock(block, doubleSlab, texture);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().slab(baseName, texture, texture, texture));
    }

    public void slabBlockWithItem(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation side, ResourceLocation top){
        slabBlock(block, doubleSlab, side, top);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().slab(baseName, side, top, top));
    }

    public void slabBlock(SlabBlock block, ResourceLocation texture) {
        slabBlock(block, texture, texture, texture, texture);
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleSlab, ResourceLocation side, ResourceLocation top) {
        slabBlock(block, doubleSlab, side, top, top);
    }

    public void stairsBlockWithItem(StairBlock block, ResourceLocation texture) {
        stairsBlock(block, texture, texture, texture);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().stairs(baseName, texture, texture, texture));
    }

    public void buttonBlockWithItem(ButtonBlock block, ResourceLocation texture){
        buttonBlock(block, texture);
        buttonBlockInventory(block, texture);
        simpleBlockItem(block, buttonBlockInventory(block, texture));
    }

    public ModelFile buttonBlockInventory(ButtonBlock block, ResourceLocation texture){
        String baseName = key(block).toString();
        return models().buttonInventory(baseName + "_inventory", texture);
    }

    public void logBlockWithItem(RotatedPillarBlock block){
        logBlock(block);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().cubeColumn(baseName, blockTexture(block), extend(blockTexture(block), "_top")));
    }

    public void columnBlockWithItem(RotatedPillarBlock block, ResourceLocation texture){
        columnBlockWithItem(block, texture, texture);
    }

    public void columnBlockWithItem(RotatedPillarBlock block, ResourceLocation side, ResourceLocation top){
        columnBlock(block, side, top);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().cubeColumn(baseName, side, top));
    }

    public void columnBlock(RotatedPillarBlock block, ResourceLocation texture){
        columnBlock(block, texture, texture);
    }

    public void columnBlock(RotatedPillarBlock block, ResourceLocation side, ResourceLocation top){
        axisBlock(block,
                models().cubeColumn(name(block), side, top),
                models().cubeColumn(name(block), side, top));
    }

    public void sideBottomTopBlock(Block block, ResourceLocation top, ResourceLocation side, ResourceLocation bottom){
        String baseName = key(block).toString();
        models().cube(baseName, bottom, top, side, side, side, side).texture("particle", side);
        simpleBlockWithItem(block, models().cube(baseName, bottom, top, side, side, side, side).texture("particle", side));
    }

    public void sideBottomTopColumnBlock(RotatedPillarBlock block, ResourceLocation top, ResourceLocation side, ResourceLocation bottom){
        String baseName = key(block).toString();
        axisBlock(block,
                models().cube(baseName, bottom, top, side, side, side, side).texture("particle", side),
                models().cube(baseName, bottom, top, side, side, side, side).texture("particle", side));
        simpleBlockItem(block, models().cube(baseName, bottom, top, side, side, side, side).texture("particle", side));
    }

    public void fenceGateWithItem(FenceGateBlock block, ResourceLocation texture){
        fenceGateBlock(block, texture);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().fenceGate(baseName, texture));
    }

    public void fenceBlockWithItem(FenceBlock block, ResourceLocation texture){
        fenceBlock(block, texture);
        fenceInventory(block, texture);
        simpleBlockItem(block, fenceInventory(block, texture));
    }

    public ModelFile fenceInventory(FenceBlock block, ResourceLocation texture){
        String baseName = key(block).toString();
        return models().fenceInventory(baseName + "_inventory", texture);
    }

    public void fenceBlock(FenceBlock block, ResourceLocation texture) {
        String baseName = key(block).toString();
        fourWayBlock(block,
                models().fencePost(baseName + "_post", texture),
                models().fenceSide(baseName + "_side", texture));
        fenceInventory(block, texture);
    }

    public void pressurePlateWithItem(PressurePlateBlock block, ResourceLocation texture){
        pressurePlateBlock(block, texture);
        String baseName = key(block).toString();
        simpleBlockItem(block, models().pressurePlate(baseName, texture));
    }

    public void wallBlockWithItem(WallBlock block, ResourceLocation texture){
        wallBlock(block, texture);
        wallBlockInventory(block, texture);
        simpleBlockItem(block, wallBlockInventory(block, texture));
    }

    public void wallBlock(WallBlock block, ResourceLocation texture) {
        wallBlockInternal(block, key(block).toString(), texture);
    }

    public ModelFile wallBlockInventory(WallBlock block, ResourceLocation texture){
        String baseName = key(block).toString();
        return models().wallInventory(baseName + "_inventory", texture);
    }

    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) {
        wallBlock(block, models().wallPost(baseName + "_post", texture),
                models().wallSide(baseName + "_side", texture),
                models().wallSideTall(baseName + "_side_tall", texture));
    }

    public void paneBlockWithItem(IronBarsBlock block, ResourceLocation texture) {
        paneBlockWithItem(block, texture, texture, texture);
    }

    public void paneBlockWithItem(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge, ResourceLocation item) {
        paneBlock(block, pane, edge);
        itemModels().getBuilder(key(block).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", item);
    }

    public void paneBlockWithItemAndRender(IronBarsBlock block, ResourceLocation texture, String renderType) {
        paneBlockWithItemAndRender(block, texture, texture, texture, renderType);
    }

    public void paneBlockWithItemAndRender(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge, ResourceLocation item, String renderType) {
        paneBlockWithRenderType(block, pane, edge, renderType);
        itemModels().getBuilder(key(block).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", item);
    }

    public void trapdoorBlockWithItem(TrapDoorBlock block, ResourceLocation texture, boolean orientable) {
        trapdoorBlock(block, texture, orientable);
        simpleBlockItem(block, models().trapdoorOrientableBottom(name(block), texture));
    }

    protected void builtinEntity(Block b, String particle) {
        simpleBlock(b, models().getBuilder(name(b))
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", particle));
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

}
