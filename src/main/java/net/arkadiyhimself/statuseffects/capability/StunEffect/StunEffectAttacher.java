package net.arkadiyhimself.statuseffects.capability.StunEffect;

import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

public class StunEffectAttacher extends CapabilityAttacher {
    private static final Class<StunEffect> STUN_EFFECT_CLASS = StunEffect.class;
    public static final Capability<StunEffect> STUN_EFFECT_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation STUN_EFFECT_CAPABILITY_RL = new ResourceLocation(StatusEffects.MODID, "properties_freeze");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static StunEffect getStunEffectUnwrap(LivingEntity entity) {
        return getStunEffect(entity).orElse(null);
    }

    public static LazyOptional<StunEffect> getStunEffect(LivingEntity entity) {
        return entity.getCapability(STUN_EFFECT_CAPABILITY);
    }
    private static void attacher(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new StunEffect(entity), STUN_EFFECT_CAPABILITY, STUN_EFFECT_CAPABILITY_RL);
    }
    public static void register() {
        CapabilityAttacher.registerCapability(STUN_EFFECT_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, StunEffectAttacher::attacher, StunEffectAttacher::getStunEffect, true);
    }

}
