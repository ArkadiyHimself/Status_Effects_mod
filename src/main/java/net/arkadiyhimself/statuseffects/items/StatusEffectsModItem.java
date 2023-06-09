package net.arkadiyhimself.statuseffects.items;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StatusEffectsModItem {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, StatusEffects.MODID);

/*    public static final RegistryObject<Item> PLUTONIUM = ITEMS.register("plutonium",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)));
    public static final RegistryObject<Item> RAW_PLUTONIUM = ITEMS.register("raw_plutonium",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)));
*/

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
