package net.arkadiyhimself.statuseffects.effects.effectsdostuff;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;


public class stunning extends MobEffect {

    boolean returnAI = false;

    public stunning() {
        super(MobEffectCategory.HARMFUL, 13859964);

    }
    public stunning(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity p_19467_, int p_19468_) {
        if (returnAI && p_19467_ instanceof Mob mob) {
            for (Goal.Flag flag : Goal.Flag.values()) {
                mob.goalSelector.enableControlFlag(flag);
                mob.targetSelector.enableControlFlag(flag);
            }
        }
        super.applyEffectTick(p_19467_, p_19468_);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int p_19456_) {
        returnAI = pDuration < 5;
        return true;
    }
}
