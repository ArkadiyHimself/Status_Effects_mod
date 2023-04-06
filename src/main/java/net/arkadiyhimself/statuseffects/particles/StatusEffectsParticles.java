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

    public static final RegistryObject<SimpleParticleType> DOOMED_SOUL1 = PARTICLES.register("doomed_soul1",
            () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DOOMED_SOUL2 = PARTICLES.register("doomed_soul2",
            () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DOOMED_SOUL3 = PARTICLES.register("doomed_soul3",
            () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FALLEN_SOUL = PARTICLES.register("fallen_soul",
            () -> new SimpleParticleType(true));
}
