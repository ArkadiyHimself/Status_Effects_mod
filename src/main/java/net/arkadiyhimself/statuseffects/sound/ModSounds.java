package net.arkadiyhimself.statuseffects.sound;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.
            create(ForgeRegistries.SOUND_EVENTS, Status_Effects.MODID);

    private static RegistryObject<SoundEvent> registerSoundEvent(String path) {
        return SOUND_EVENTS.register(path, () -> SoundEvent.createFixedRangeEvent
                (new ResourceLocation(Status_Effects.MODID, path), 1F));
    }

    public static final RegistryObject<SoundEvent> RINGING_DEAF =
            registerSoundEvent("ringing_deaf");

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
