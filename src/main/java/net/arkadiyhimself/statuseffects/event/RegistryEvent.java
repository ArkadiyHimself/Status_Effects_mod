package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.arkadiyhimself.statuseffects.particles.SE_Particles;
import net.arkadiyhimself.statuseffects.particles.custom.DoomedParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Status_Effects.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvent {

    @SubscribeEvent
    static void registerParticles(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(SE_Particles.DOOMED_SOUL.get(),
                DoomedParticles.Provider::new);
    }
}
