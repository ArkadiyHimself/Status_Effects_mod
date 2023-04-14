package net.arkadiyhimself.statuseffects.mixin;


import net.arkadiyhimself.statuseffects.attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.WitherSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract boolean canFreeze();
    @Shadow public abstract boolean hasEffect(MobEffect pEffect);
    ArrayList<Class<?>> soulless = new ArrayList<>(){{
        add(Skeleton.class);
        add(WitherSkeleton.class);
        add(Stray.class);
    }};

    @Inject(at = @At(value = "HEAD"), method = "canBeAffected", cancellable = true)
    protected void immunity(MobEffectInstance pEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        if(!this.canFreeze() && pEffectInstance.getEffect() == StatusEffectsMobEffect.FREEZE.get()) {
            cir.setReturnValue(false);
        }
        for (Class<?> aClass : soulless) {
            if (this.getClass() == aClass && pEffectInstance.getEffect() == StatusEffectsMobEffect.DOOMED.get()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "getJumpBoostPower()D", cancellable = true)
    protected void disableJumpStun(CallbackInfoReturnable<Double> cir) {
        if (this.hasEffect(StatusEffectsMobEffect.STUN.get()) || this.hasEffect(StatusEffectsMobEffect.FREEZE.get())) {
            cir.setReturnValue((double)-100F);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "swing(Lnet/minecraft/world/InteractionHand;)V", cancellable = true)
    protected void cancelSwing(InteractionHand pHand, CallbackInfo ci) {
        if (this.hasEffect(StatusEffectsMobEffect.DISARM.get())) { ci.cancel(); }
    }
    @Inject(at = @At(value = "HEAD"), method = "getCurrentSwingDuration", cancellable = true)
    protected void slowSwing(CallbackInfoReturnable<Integer> cir) {
        if (this.hasEffect(StatusEffectsMobEffect.FREEZE.get())) { cir.setReturnValue(12); }
    }
}
