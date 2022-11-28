package com.nekotune.scorch.common.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("modtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.PLANT_FIBER.get());
        }
    };
}
