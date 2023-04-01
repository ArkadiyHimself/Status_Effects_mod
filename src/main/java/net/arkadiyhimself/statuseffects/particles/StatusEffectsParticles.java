package net.arkadiyhimself.statuseffects.particles;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StatusEffectsParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, StatusEffects.MODID);

    public static void register(IEventBus eventBus) { PARTICLES.register(eventBus); }

    public static final RegistryObject<SimpleParticleType> DOOMED_SOUL = PARTICLES.register("doomed_soul",
            () -> new SimpleParticleType(true));

}
