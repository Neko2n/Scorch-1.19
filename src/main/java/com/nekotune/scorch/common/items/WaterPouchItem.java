package com.nekotune.scorch.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class WaterPouchItem extends Item {
    private int charges;
    private final int MAX_CHARGES;

    public WaterPouchItem(Properties properties, int max_charges) {
        super(properties);
        this.MAX_CHARGES = max_charges;
        this.charges = 0;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, itemStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }
        if (entity instanceof Player && !((Player)entity).getAbilities().instabuild) {
            charges = Math.max(charges-1, 0);
        }
        return itemStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos blockPos = blockHitResult.getBlockPos();
        Direction direction = blockHitResult.getDirection();
        BlockPos blockPos1 = blockPos.relative(direction);
        BlockState blockState = level.getBlockState(blockPos);
        if (player.mayInteract(level, blockPos) && player.mayUseItemAt(blockPos1, direction, itemStack)) {
            boolean flag1 = blockState.getFluidState().isSourceOfType(Fluids.WATER);
            boolean flag2 = false; // Block that contains fluid like cauldrons or sheared barrel cactus
            boolean flag3 = blockState.getBlock() instanceof BucketPickup;
            if ((flag1 || flag2) && flag3 && charges < MAX_CHARGES) {
                level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState()); // Change this to instead work like buckets when I add functionality for cauldrons etc.
                BucketPickup bucketPickup = (BucketPickup)blockState.getBlock();
                ItemStack itemStack1 = bucketPickup.pickupBlock(level, blockPos, blockState);
                if (!itemStack1.isEmpty()) {
                    player.awardStat(Stats.ITEM_USED.get(this));
                    bucketPickup.getPickupSound(blockState).ifPresent((soundEvent) -> {
                        player.playSound(soundEvent, 1.0F, 1.0F);
                    });
                    level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                    if (!level.isClientSide) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemStack1);
                    }
                    charges = MAX_CHARGES;
                    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
                }
            }
            if (charges > 0) {
                return ItemUtils.startUsingInstantly(level, player, hand);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
