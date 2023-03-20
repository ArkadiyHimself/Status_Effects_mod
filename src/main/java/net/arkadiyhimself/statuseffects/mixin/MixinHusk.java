package net.arkadiyhimself.statuseffects.mixin;

import net.arkadiyhimself.statuseffects.effects.ModMobEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Husk.class)
public class MixinHusk extends Monster {
    public MixinHusk(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
    /*    @Inject(at = @At(value = "TAIL"), method = "Lnet/minecraft/world/entity/monster/Husk;doHurtTarget(Lnet/minecraft/world/entity/Entity;)Z")
    protected void applyStun(Entity p_32892_, CallbackInfoReturnable<Boolean> cir) {
        float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
        ((LivingEntity)p_32892_).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)f), this);
        ((LivingEntity)p_32892_).addEffect(new MobEffectInstance(ModMobEffect.STUN.get(), 140));
    } */

    public boolean doHurtTarget(Entity p_32892_) {
        boolean flag = super.doHurtTarget(p_32892_);
        if (flag && p_32892_ instanceof LivingEntity) {
        ((LivingEntity)p_32892_).addEffect(new MobEffectInstance(ModMobEffect.STUN.get(), 140)); }
        return flag;
    }
}
