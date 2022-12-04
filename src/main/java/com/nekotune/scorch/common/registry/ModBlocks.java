package com.nekotune.scorch.common.registry;

import com.nekotune.scorch.Scorch;
import com.nekotune.scorch.common.blocks.BrittleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Scorch.MOD_ID);

    public static final RegistryObject<Block> CORRODED_STONE = registerBlock("corroded_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f, 6.0f).requiresCorrectToolForDrops()), ModCreativeTabs.MOD_TAB);
    public static final RegistryObject<Block> BRITTLE_CORRODED_STONE = registerBlock("brittle_corroded_stone",
            () -> new BrittleBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f, 6.0f).requiresCorrectToolForDrops()), ModCreativeTabs.MOD_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
