package net.arkadiyhimself.statuseffects.mobeffects.effectsdostuff;

import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;


public class Stunning extends MobEffect {

    boolean returnAI = false;
    int currentDuration;

    public Stunning() {
        super(MobEffectCategory.HARMFUL, 13859964);

    }
    public Stunning(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (returnAI && pLivingEntity instanceof Mob mob) {
            for (Goal.Flag flag : Goal.Flag.values()) {
                mob.goalSelector.enableControlFlag(flag);
                mob.targetSelector.enableControlFlag(flag);
            }
        }
        StunScaleAttacher.getStunScale(pLivingEntity).ifPresent(stunScale -> {
            stunScale.setCurrentDuration(currentDuration);
            if (returnAI) {
                stunScale.setStunned(false);
            }
            stunScale.updateData();
        });
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        returnAI = pDuration < 2;
        currentDuration = pDuration;
        return true;
    }
}
