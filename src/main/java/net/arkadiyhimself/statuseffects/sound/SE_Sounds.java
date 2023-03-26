package net.arkadiyhimself.statuseffects.sound;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SE_Sounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.
            create(ForgeRegistries.SOUND_EVENTS, Status_Effects.MODID);

   public static RegistryObject<SoundEvent> registerSoundEvent(String path) {
        return SOUND_EVENTS.register(path, () -> SoundEvent.createFixedRangeEvent
                (new ResourceLocation(Status_Effects.MODID, path), 1F));
    }

    public static final RegistryObject<SoundEvent> RINGING_DEAF =
            registerSoundEvent("ringing_deaf");
    public static final RegistryObject<SoundEvent> RINGING_LONG =
            registerSoundEvent("ringing_long");
    public static final RegistryObject<SoundEvent> DOOMED =
            registerSoundEvent("doomed");

    public static final RegistryObject<SoundEvent> UNDOOMED =
            registerSoundEvent("undoomed");

    public static final RegistryObject<SoundEvent> WHISPER1 =
            registerSoundEvent("whisper1");

    public static final RegistryObject<SoundEvent> WHISPER2 =
            registerSoundEvent("whisper2");

    public static final RegistryObject<SoundEvent> WHISPER3 =
            registerSoundEvent("whisper3");

    public static final RegistryObject<SoundEvent> WHISPER4 =
            registerSoundEvent("whisper4");

    public static final RegistryObject<SoundEvent> WHISPER5 =
            registerSoundEvent("whisper5");



    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
