package com.nekotune.scorch.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class UsableBlockItem extends BlockItem {
    public UsableBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        if (pContext.getPlayer().isCrouching()) {
            return super.canPlace(pContext, pState);
        } else return false;
    }
}
