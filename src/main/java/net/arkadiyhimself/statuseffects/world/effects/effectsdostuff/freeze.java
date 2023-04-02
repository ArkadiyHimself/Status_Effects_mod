package net.arkadiyhimself.statuseffects.world.effects.effectsdostuff;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class freeze extends MobEffect {
    public freeze(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_.fireImmune() || p_19467_.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            p_19467_.hurt(DamageSource.MAGIC, 1.0F);
        }
        super.applyEffectTick(p_19467_, p_19468_);

    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) { return true; }

}
