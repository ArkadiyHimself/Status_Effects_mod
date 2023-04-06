package net.arkadiyhimself.statuseffects.sound;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.stringtemplate.v4.ST;

public class StatusEffectsSounds {
 public final String path;
 public final RegistryObject<SoundEvent> soundEventRegistryObject;
 public StatusEffectsSounds(String path) {
  this.path = path;
  this.soundEventRegistryObject = registerSoundEvent(this.path);
 }
 public SoundEvent getSound() {
  return this.soundEventRegistryObject.get();
 }
 public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.
         create(ForgeRegistries.SOUND_EVENTS, StatusEffects.MODID);

 public RegistryObject<SoundEvent> registerSoundEvent(String path) {
  return SOUND_EVENTS.register(path, () -> SoundEvent.createFixedRangeEvent
          (new ResourceLocation(StatusEffects.MODID, path), 1F));
 }
 public static final StatusEffectsSounds RINGING_LONG = new StatusEffectsSounds("ringing_long");
 public static final StatusEffectsSounds UNDOOMED = new StatusEffectsSounds("undoomed");
 public static final StatusEffectsSounds DOOMED = new StatusEffectsSounds("doomed");
 public static final StatusEffectsSounds FALLEN_BREATH = new StatusEffectsSounds("fallen_breath");
 public static void register(IEventBus eventBus) {
  SOUND_EVENTS.register(eventBus);
 }

}
