package net.arkadiyhimself.statuseffects.effects.effectsdostuff;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.arkadiyhimself.statuseffects.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.warden.Warden;
import org.jetbrains.annotations.Nullable;

public class deafening extends MobEffect {

    public deafening(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }


    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        p_19467_.playSound(ModSounds.RINGING_DEAF.get(), 0.15F, 1.0F);
        p_19467_.hurt(DamageSource.MAGIC, 1F);
        if(p_19467_ instanceof Warden) {

        }
        super.applyEffectTick(p_19467_, p_19468_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) { return true; }
}
