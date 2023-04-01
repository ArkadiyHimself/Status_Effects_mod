package net.arkadiyhimself.statuseffects.mixin;

import net.arkadiyhimself.statuseffects.effects.StatusEffectsMobEffect;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract boolean canFreeze();


    @Shadow protected abstract float tickHeadTurn(float pYRot, float pAnimStep);

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

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/world/entity/LivingEntity;getJumpBoostPower()D", cancellable = true)
    protected void disableJumpStun(CallbackInfoReturnable<Double> cir) {
        if (this.hasEffect(StatusEffectsMobEffect.STUN.get())) {
            cir.setReturnValue((double)-1000F);
        }
    }
}
