package com.nekotune.scorch.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;

// Unfinished
public class GiantBubbleCactusBlock extends AbstractCauldronBlock {
    public static final BooleanProperty SHEARED = BooleanProperty.create("sheared");
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;

    public GiantBubbleCactusBlock(Properties pProperties) {
        super(pProperties, CauldronInteraction.WATER);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHEARED, Boolean.FALSE));
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 1));
    }

    @Override
    public boolean isFull(BlockState pState) {
        return pState.getValue(LEVEL) == 1;
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide && pState.getValue(SHEARED) && pEntity.isOnFire() && this.isEntityInsideContent(pState, pPos, pEntity)) {
            pEntity.clearFire();
            if (pEntity.mayInteract(pLevel, pPos)) {
                BlockState emptyState = pState.setValue(LEVEL, 0);
                pLevel.setBlockAndUpdate(pPos, emptyState);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(emptyState));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LEVEL);
    }
}
