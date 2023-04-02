package net.arkadiyhimself.statuseffects.attributes;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StatusEffectsAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTE =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, StatusEffects.MODID);
    public static void register(IEventBus eventBus) {
        ATTRIBUTE.register(eventBus);
    }

    public static final RegistryObject<Attribute> STUN_POINTS = ATTRIBUTE.register("stun_points",
            () -> new RangedAttribute("attribute.name.generic.max_stun",
                    0.0D, 0.0D, 1000.0D).setSyncable(true));

}
