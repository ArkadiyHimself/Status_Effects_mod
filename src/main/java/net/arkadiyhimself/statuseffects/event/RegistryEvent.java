package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.Attributes.SE_Attributes;
import net.arkadiyhimself.statuseffects.Status_Effects;
import net.arkadiyhimself.statuseffects.particles.SE_Particles;
import net.arkadiyhimself.statuseffects.particles.custom.DoomedParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Status_Effects.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvent {

    @SubscribeEvent
    static void registerParticles(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(SE_Particles.DOOMED_SOUL.get(),
                DoomedParticles.Provider::new);
    }
    @SubscribeEvent
    static void customAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            if (!event.has(entityType, SE_Attributes.STUN_POINTS.get())) {
                event.add(entityType, SE_Attributes.STUN_POINTS.get());
            }
        });
    }
}
