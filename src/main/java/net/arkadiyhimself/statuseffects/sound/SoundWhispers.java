package net.arkadiyhimself.statuseffects.sound;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class SoundWhispers {
    public static final List<SoundWhispers> whispers = new ArrayList<>();
    private final String path;
    public static int amount = 0;
    private final RegistryObject<SoundEvent> soundEventRegistryObject;
    public SoundWhispers(String path) {
        this.path = path;
        this.soundEventRegistryObject = registerSoundWhisper("whispers." + this.path);
        amount++;
        whispers.add(this);
    }
    public SoundEvent getSound() {
        return this.soundEventRegistryObject.get();
    }
    public static final DeferredRegister<SoundEvent> SOUND_WHISPERS = DeferredRegister.
            create(ForgeRegistries.SOUND_EVENTS, StatusEffects.MODID);
    public static RegistryObject<SoundEvent> registerSoundWhisper(String path) {
        return SOUND_WHISPERS.register(path, () -> SoundEvent.createFixedRangeEvent
                (new ResourceLocation(StatusEffects.MODID, path), 1F));
    }
    public static final SoundWhispers WHISPER1 = new SoundWhispers("whisper1");
    public static final SoundWhispers WHISPER2 = new SoundWhispers("whisper2");
    public static final SoundWhispers WHISPER3 = new SoundWhispers("whisper3");
    public static final SoundWhispers WHISPER4 = new SoundWhispers("whisper4");
    public static final SoundWhispers WHISPER5 = new SoundWhispers("whisper5");
    public static void register(IEventBus eventBus) { SOUND_WHISPERS.register(eventBus); }
}
