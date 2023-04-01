package net.arkadiyhimself.statuseffects.mixin.StunMechanic;

import net.arkadiyhimself.statuseffects.Attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.effects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.interfaces.StunScaleInterface;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class StunLivingEntity implements StunScaleInterface {
    @Shadow public abstract boolean hasEffect(MobEffect pEffect);

    @Shadow @Final private AttributeMap attributes;

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract double getAttributeValue(Attribute pAttribute);

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute pAttribute);

    @Shadow public abstract double getAttributeValue(Holder<Attribute> p_251296_);

    @Shadow public abstract boolean addEffect(MobEffectInstance pEffectInstance);

    @Override
    public boolean isStunned() {
        return this.hasEffect(StatusEffectsMobEffect.STUN.get());
    }
    int MAX_STUN_POINTS = 100;

    @Override
    public int getCurrentStunPoints() {
        return (int) Math.ceil(this.getAttributeValue(StatusEffectsAttributes.STUN_POINTS.get()));
    }

    @Override
    public int getMaxStunPoints() {
        return MAX_STUN_POINTS;
    }
    public AttributeModifier stunAdd(int amount) {
        return new AttributeModifier("add stun points", amount,
                AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void addStunPoints(int amount) {
        if(this.getAttributeValue(StatusEffectsAttributes.STUN_POINTS.get()) + amount < MAX_STUN_POINTS) {
            this.getAttribute(StatusEffectsAttributes.STUN_POINTS.get()).addPermanentModifier(stunAdd(amount));
        } else {
            this.getAttribute(StatusEffectsAttributes.STUN_POINTS.get()).addPermanentModifier(stunAdd(MAX_STUN_POINTS));
            this.addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), 50));
        }
    }
}
