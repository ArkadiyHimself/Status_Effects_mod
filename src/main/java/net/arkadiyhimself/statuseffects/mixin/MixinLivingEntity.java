package net.arkadiyhimself.statuseffects.mixin;

import net.arkadiyhimself.statuseffects.effects.SE_MobEffect;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract boolean canFreeze();

    @Inject(at = @At(value = "HEAD"), method = "canBeAffected", cancellable = true)
    protected void immuneToFreeze(MobEffectInstance pEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        if(!this.canFreeze() && pEffectInstance.getEffect() == SE_MobEffect.FREEZE.get()) {
            cir.setReturnValue(false);
        }
    }
}
