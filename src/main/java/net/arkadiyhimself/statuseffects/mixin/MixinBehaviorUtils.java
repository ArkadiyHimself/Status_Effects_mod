package net.arkadiyhimself.statuseffects.mixin;


import net.arkadiyhimself.statuseffects.world.effects.StatusEffectsMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BehaviorUtils.class)
public class MixinBehaviorUtils {

    @Inject(at = @At(value = "TAIL"), method = "isWithinAttackRange", cancellable = true)
    protected static void disarm(Mob pMob, LivingEntity pTarget, int pCooldown, CallbackInfoReturnable<Boolean> cir) {
        if(pMob.hasEffect(StatusEffectsMobEffect.DISARM.get())) {
            cir.setReturnValue(false);
        }
    }
}
