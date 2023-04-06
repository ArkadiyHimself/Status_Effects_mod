package net.arkadiyhimself.statuseffects.mobeffects.effectsdostuff;

import net.arkadiyhimself.statuseffects.capability.FreezeEffectAttacher;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class Freeze extends MobEffect {
    boolean noLongerFrozen;
    int duration;
    public Freeze(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.fireImmune() || pLivingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            pLivingEntity.hurt(DamageSource.MAGIC, 1.0F);
        }
        FreezeEffectAttacher.getHasFreeze(pLivingEntity).ifPresent(hasFreezeEffect -> {
            hasFreezeEffect.setCurrentDuration(duration);
            if(noLongerFrozen) {
                hasFreezeEffect.setFrozen(false);
            }
            hasFreezeEffect.updateData();
        });
        super.applyEffectTick(pLivingEntity, pAmplifier);

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        noLongerFrozen = pDuration < 2;
        duration = pDuration;
        return true;
    }

}
