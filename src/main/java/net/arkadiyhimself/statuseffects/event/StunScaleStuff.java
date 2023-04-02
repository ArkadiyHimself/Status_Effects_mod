package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.StunScale;
import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class StunScaleStuff {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(StunScale.class);
    }
    @SubscribeEvent
    public static void stunPointsDecay(LivingEvent.LivingTickEvent event) {
        StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
            stunScale.subDecayDelay(1, true);
            if (stunScale.getDecayDelay() == 0) {
                stunScale.subStunPoints(1, true);
            }
        });
    }
    @SubscribeEvent
    public static void stunApplied(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                stunScale.setStunned(true, true);
            });
        }
    }
    @SubscribeEvent
    public static void stunEnded(MobEffectEvent.Remove event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                stunScale.setStunned(false, true);
            });
        }
    }
    @SubscribeEvent
    public static void pointsOnHit(LivingDamageEvent event) {
        if (!event.getEntity().hasEffect(StatusEffectsMobEffect.STUN.get())) {
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                if (event.getAmount() > 0) {
                    stunScale.addStunPoints(50, false);
                    stunScale.setDecayDelay(50, true);
                }
                if (stunScale.getStunPoints() == stunScale.getMaxStunPoints()) {
                    stunScale.setStunPoints(0, false);
                    stunScale.setDecayDelay(0, true);
                    int duration = stunScale.getDefaultStunDurationFromHits();
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), duration));
                }
            });
        }
    }
}
