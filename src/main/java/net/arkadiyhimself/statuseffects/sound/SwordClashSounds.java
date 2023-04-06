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

public class SwordClashSounds {
    public static final List<SwordClashSounds> swordClashes = new ArrayList<>();
    public final String path;
    public static int amount = 0;
    public final RegistryObject<SoundEvent> soundEventRegistryObject;
    public SwordClashSounds(String path) {
        this.path = path;
        this.soundEventRegistryObject = registerSoundSwordClash("swordclash." + this.path);
        amount++;
        swordClashes.add(this);
    }
    public SoundEvent getSound() { return this.soundEventRegistryObject.get(); }
    public static final DeferredRegister<SoundEvent> SWORD_CLASH_SOUNDS = DeferredRegister.
            create(ForgeRegistries.SOUND_EVENTS, StatusEffects.MODID);
    public static RegistryObject<SoundEvent> registerSoundSwordClash(String path) {
        return SWORD_CLASH_SOUNDS.register(path, () -> SoundEvent.createFixedRangeEvent
                (new ResourceLocation(StatusEffects.MODID, path), 1F));
    }
    public static final SwordClashSounds SWORDCLASH1 = new SwordClashSounds("sword_clashing1");
    public static final SwordClashSounds SWORDCLASH2 = new SwordClashSounds("sword_clashing2");
    public static final SwordClashSounds SWORDCLASH3 = new SwordClashSounds("sword_clashing3");
    public static final SwordClashSounds SWORDCLASH4 = new SwordClashSounds("sword_clashing4");
    public static final SwordClashSounds SWORDCLASH5 = new SwordClashSounds("sword_clashing5");
    public static void register(IEventBus eventBus) {
        SWORD_CLASH_SOUNDS.register(eventBus);
    }
}
