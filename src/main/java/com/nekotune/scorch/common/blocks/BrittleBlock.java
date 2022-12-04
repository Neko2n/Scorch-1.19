package com.nekotune.scorch.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BrittleBlock extends Block {
    private float breakChance = 0.5f;
    private byte life = 1;

    private Random random = new Random();

    public BrittleBlock(Properties properties) {
        super(properties);
    }
    public BrittleBlock(Properties properties, float breakChance) {
        super(properties);
        this.breakChance = breakChance;
    }

    // Doesn't work at the moment
    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (entity instanceof Player && random.nextFloat() <= breakChance) {
            level.destroyBlockProgress(entity.getId(), blockPos, -2);
        }
        super.stepOn(level, blockPos, blockState, entity);
    }
}
