package com.nekotune.scorch.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BrittleBlock extends Block {
    private static final Random random = new Random();

    public BrittleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (pEntity instanceof LivingEntity) {
            // Spawn particles to indicate block is brittle
            pLevel.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, pState).setPos(pPos),
                    pPos.getX() + random.nextFloat(), random.nextFloat() * 0.8d + pPos.getY() + 0.1d,
                    pPos.getZ() + random.nextFloat(), (random.nextFloat() - 0.5d) * 4.0d,
                    0.8d, (random.nextFloat() - 0.5d) * 4.0d);
            if (!pLevel.isClientSide) {
                boolean flag1 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, (LivingEntity) pEntity) == 0; // No feather falling
                boolean flag2 = !((LivingEntity) pEntity).hasEffect(MobEffects.SLOW_FALLING); // No slow falling effect
                boolean flag3 = (!pEntity.isSteppingCarefully() || pEntity.fallDistance > 3); // Either not crouching or falling from a distance of over 3 blocks
                if (random.nextFloat() < Math.max(pEntity.fallDistance * 25.0f, 1.0f) * 0.01f && flag1 && flag2 && flag3) {
                    // Block break effects
                    pLevel.levelEvent(1022, pPos, 0);
                    pLevel.levelEvent(2001, pPos, Block.getId(pState));
                    // Drop block at 50% chance
                    if (random.nextFloat() <= 0.5f) {
                        dropResources(pState, pLevel, pPos, null);
                    }
                    pLevel.removeBlock(pPos, false);
                }
            }
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
