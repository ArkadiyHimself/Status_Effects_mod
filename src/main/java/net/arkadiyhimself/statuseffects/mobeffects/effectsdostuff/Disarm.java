package net.arkadiyhimself.statuseffects.mobeffects.effectsdostuff;

import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffectAttacher;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Disarm extends MobEffect {
    public Disarm(MobEffectCategory pCategory, int pColor) { super(pCategory, pColor); }
    public boolean undisarmed;
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (undisarmed) {
            DisarmEffectAttacher.getHasDisarm(pLivingEntity).ifPresent(disarmEffect -> {
                disarmEffect.setDisarmed(false);
                disarmEffect.updateTracking();
            });
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        undisarmed = pDuration < 2;
        return true;
    }
}
