package net.arkadiyhimself.statuseffects.effects.effectsdostuff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;


public class deafening extends MobEffect {

    public deafening(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }


    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_ instanceof Warden) {
        }
        super.applyEffectTick(p_19467_, p_19468_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) { return true; }
}
