package net.arkadiyhimself.statuseffects.effects.effectsdostuff;

import net.arkadiyhimself.statuseffects.sound.ModSounds;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class doomed extends MobEffect {

    boolean doplaysound = false;

    public doomed(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        doplaysound = p_19455_ == 20;
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(doplaysound){ p_19467_.playSound(ModSounds.UNDOOMED.get(), 5F, 1F); }
        super.applyEffectTick(p_19467_, p_19468_);
    }

}
