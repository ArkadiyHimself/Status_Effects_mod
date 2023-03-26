package net.arkadiyhimself.statuseffects.particles;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SE_Particles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Status_Effects.MODID);

    public static void register(IEventBus eventBus) { PARTICLES.register(eventBus); }

    public static final RegistryObject<SimpleParticleType> DOOMED_SOUL = PARTICLES.register("doomed_soul",
            () -> new SimpleParticleType(true));

}
