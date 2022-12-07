package com.nekotune.scorch.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterPouchItem extends Item {
    private int charges;
    private final int MAX_CHARGES;

    public WaterPouchItem(Properties properties, int max_charges) {
        super(properties);
        this.MAX_CHARGES = max_charges;
        this.charges = 0;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, pStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            charges = Math.max(charges-1, 0);
        }
        return pStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos blockPos = blockHitResult.getBlockPos();
        Direction direction = blockHitResult.getDirection();
        BlockPos blockPos1 = blockPos.relative(direction);
        BlockState blockState = pLevel.getBlockState(blockPos);
        if (pPlayer.mayInteract(pLevel, blockPos) && pPlayer.mayUseItemAt(blockPos1, direction, itemStack)) {
            boolean flag1 = blockState.getFluidState().isSourceOfType(Fluids.WATER);
            boolean flag2 = false; // Block that contains fluid like cauldrons or sheared barrel cactus
            boolean flag3 = blockState.getBlock() instanceof BucketPickup;
            if ((flag1 || flag2) && flag3 && charges < MAX_CHARGES) {
                BucketPickup bucketPickup = (BucketPickup)blockState.getBlock();
                ItemStack itemStack1 = bucketPickup.pickupBlock(pLevel, blockPos, blockState);
                if (!itemStack1.isEmpty()) {
                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    bucketPickup.getPickupSound(blockState).ifPresent((soundEvent) -> {
                        pPlayer.playSound(soundEvent, 1.0F, 1.0F);
                    });
                    pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockPos);
                    if (!pLevel.isClientSide) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)pPlayer, itemStack1);
                    }
                    charges = MAX_CHARGES;
                    return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
                }
                pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3); // Change this to instead work like buckets when I add functionality for cauldrons etc.
            }
            if (charges > 0) {
                return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
