package com.nekotune.scorch.common.registry;

import com.nekotune.scorch.Scorch;
import com.nekotune.scorch.common.items.WaterPouchItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Scorch.MOD_ID);

    public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register("plant_fiber",
            () -> new Item(new Item.Properties().tab(ModCreativeTabs.MOD_TAB)));

    public static final RegistryObject<Item> WATER_POUCH = ITEMS.register("water_pouch",
            () -> new WaterPouchItem(new Item.Properties().tab(ModCreativeTabs.MOD_TAB).stacksTo(1), 3));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
