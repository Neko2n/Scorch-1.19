package com.nekotune.scorch.common.registry;

import com.nekotune.scorch.Scorch;
import com.nekotune.scorch.common.blocks.BrittleBlock;
import com.nekotune.scorch.common.blocks.LightBulbBlock;
import com.nekotune.scorch.common.items.UsableBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Scorch.MOD_ID);



    public static final RegistryObject<Block> BRITTLE_CORRODED_STONE = registerBlock("brittle_corroded_stone",
            () -> new BrittleBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(1.5f, 3.0f).requiresCorrectToolForDrops()),
            new Item.Properties().tab(ModCreativeTabs.MOD_TAB), false);

    // Unfinished
    public static final RegistryObject<Block> BUBBLE_CACTUS = registerBlock("bubble_cactus",
            () -> new Block(BlockBehaviour.Properties.of(Material.CACTUS, MaterialColor.COLOR_GREEN)
                    .strength(1.0f, 1.0f)),
            new Item.Properties().tab(ModCreativeTabs.MOD_TAB), false);

    public static final RegistryObject<Block> CORRODED_STONE = registerBlock("corroded_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(1.5f, 6.0f).requiresCorrectToolForDrops()),
            new Item.Properties().tab(ModCreativeTabs.MOD_TAB), false);

    public static final RegistryObject<Block> LIGHT_BULB = registerBlock("light_bulb",
            () -> new LightBulbBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.SAND)
                    .strength(0.1f, 0.1f)),
            new Item.Properties().tab(ModCreativeTabs.MOD_TAB).food(ModFoods.LIGHT_BULB), true);



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Item.Properties properties, boolean usable) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> ((usable) ? new BlockItem(toReturn.get(), properties) : new UsableBlockItem(toReturn.get(), properties)));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
