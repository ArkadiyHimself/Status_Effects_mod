package net.arkadiyhimself.statuseffects.mobeffects.effectsdostuff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber({Dist.CLIENT})
public class Deafening extends MobEffect {
    public Deafening(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        super.applyEffectTick(p_19467_, p_19468_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) { return true; }
}
