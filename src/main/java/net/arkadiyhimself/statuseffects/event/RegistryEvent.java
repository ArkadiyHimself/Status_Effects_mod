package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.particles.custom.DoomedSouls;
import net.arkadiyhimself.statuseffects.particles.custom.FallenSoul;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvent {

    @SubscribeEvent
    static void registerParticles(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(StatusEffectsParticles.DOOMED_SOUL1.get(),
                DoomedSouls.Provider::new);
        Minecraft.getInstance().particleEngine.register(StatusEffectsParticles.DOOMED_SOUL2.get(),
                DoomedSouls.Provider::new);
        Minecraft.getInstance().particleEngine.register(StatusEffectsParticles.DOOMED_SOUL3.get(),
                DoomedSouls.Provider::new);
        Minecraft.getInstance().particleEngine.register(StatusEffectsParticles.FALLEN_SOUL.get(),
                FallenSoul.Provider::new);
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
