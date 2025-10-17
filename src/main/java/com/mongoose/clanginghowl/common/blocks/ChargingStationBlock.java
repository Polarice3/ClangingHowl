package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.ChargingStationBlockEntity;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class ChargingStationBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public ChargingStationBlock() {
        super(Properties.of()
                .strength(4.0F, 9.0F)
                .sound(SoundType.COPPER)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(OCCUPIED, Boolean.FALSE));
    }

    public boolean useShapeForLightOcclusion(BlockState p_52997_) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            ItemStack heldItem = player.getItemInHand(hand);
            ChargingStationBlockEntity pedestal = (ChargingStationBlockEntity) world.getBlockEntity(pos);
            if (pedestal != null) {
                pedestal.getCapability(ForgeCapabilities.ITEM_HANDLER, hit.getDirection()).ifPresent(handler -> {
                    ItemStack itemStack = handler.getStackInSlot(0);
                    if (itemStack.isEmpty() && heldItem.getItem() instanceof IEnergyItem) {
                        player.setItemInHand(hand, handler.insertItem(0, heldItem, false));
                        world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
                    } else if (!itemStack.isEmpty()) {
                        if (heldItem.isEmpty()) {
                            player.setItemInHand(hand, handler.extractItem(0, 64, false));
                        } else {
                            ItemHandlerHelper.giveItemToPlayer(player, handler.extractItem(0, 64, false));
                        }
                        world.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
                    }
                    pedestal.setChanged();
                });
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof ChargingStationBlockEntity) {
                tileentity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                    dropInventoryItems(tileentity.getLevel(), tileentity.getBlockPos(), handler);
                });
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public static void dropInventoryItems(Level worldIn, BlockPos pos, IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
        }
    }

    public RenderShape getRenderShape(BlockState p_53840_) {
        return RenderShape.MODEL;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        FluidState fluidstate = p_48781_.getLevel().getFluidState(p_48781_.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, p_48781_.getHorizontalDirection()).setValue(WATERLOGGED, flag);
    }

    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53838_) {
        p_53838_.add(FACING, WATERLOGGED, OCCUPIED);
    }

    public BlockState updateShape(BlockState p_51157_, Direction p_51158_, BlockState p_51159_, LevelAccessor p_51160_, BlockPos p_51161_, BlockPos p_51162_) {
        if (p_51157_.getValue(WATERLOGGED)) {
            p_51160_.scheduleTick(p_51161_, Fluids.WATER, Fluids.WATER.getTickDelay(p_51160_));
        }

        return super.updateShape(p_51157_, p_51158_, p_51159_, p_51160_, p_51161_, p_51162_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ChargingStationBlockEntity(p_153215_, p_153216_);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof ChargingStationBlockEntity block) {
                block.tick();
            }
        };
    }
}
