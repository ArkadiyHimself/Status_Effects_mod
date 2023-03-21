package net.arkadiyhimself.statuseffects.mixin;

import net.arkadiyhimself.statuseffects.effects.ModMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

/*    @Inject(at = @At(value = "HEAD"), method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    protected void antifreeze(MobEffectInstance p_147208_, Entity p_147209_, CallbackInfoReturnable<Boolean> cir) {
        if(!p_147209_.canFreeze() || p_147208_.getEffect() == ModMobEffect.FREEZE.get()) {
            cir.setReturnValue(false);
        }
    } */
}
