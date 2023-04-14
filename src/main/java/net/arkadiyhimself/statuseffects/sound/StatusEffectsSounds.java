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
 public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.
         create(ForgeRegistries.SOUND_EVENTS, StatusEffects.MODID);

 public static RegistryObject<SoundEvent> registerSoundEvent(String path, float range) {
  return SOUND_EVENTS.register(path, () -> SoundEvent.createFixedRangeEvent
          (new ResourceLocation(StatusEffects.MODID, path), range));
 }
 public static final RegistryObject<SoundEvent> RINGING_LONG = registerSoundEvent("ringing_long", 1F);
 public static final RegistryObject<SoundEvent> UNDOOMED = registerSoundEvent("undoomed", 1F);
 public static final RegistryObject<SoundEvent> DOOMED = registerSoundEvent("doomed", 1F);
 public static final RegistryObject<SoundEvent> FALLEN_BREATH = registerSoundEvent("fallen_breath", 8F);
 public static final RegistryObject<SoundEvent> ATTACK_DENIED = registerSoundEvent("attack_denied", 1F);
 /*
 public static final StatusEffectsSounds RINGING_LONG = new StatusEffectsSounds("ringing_long");
 public static final StatusEffectsSounds UNDOOMED = new StatusEffectsSounds("undoomed");
 public static final StatusEffectsSounds DOOMED = new StatusEffectsSounds("doomed");
 public static final StatusEffectsSounds FALLEN_BREATH = new StatusEffectsSounds("fallen_breath");
 public static final StatusEffectsSounds ATTACK_DENIED = new StatusEffectsSounds("attack_denied");
  */
 public static void register(IEventBus eventBus) {
  SOUND_EVENTS.register(eventBus);
 }

}
