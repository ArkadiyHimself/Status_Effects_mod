package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.Attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.particles.custom.DoomedParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvent {

    @SubscribeEvent
    static void registerParticles(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(StatusEffectsParticles.DOOMED_SOUL.get(),
                DoomedParticles.Provider::new);
    }
    @SubscribeEvent
    static void customAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            if (!event.has(entityType, StatusEffectsAttributes.STUN_POINTS.get())) {
                event.add(entityType, StatusEffectsAttributes.STUN_POINTS.get());
            }
        });
    }
}
