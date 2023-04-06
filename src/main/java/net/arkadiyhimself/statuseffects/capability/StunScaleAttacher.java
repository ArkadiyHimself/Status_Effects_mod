package net.arkadiyhimself.statuseffects.capability;

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

public class StunScaleAttacher extends CapabilityAttacher {
    private static final Class<StunScale> STUN_SCALE_CLASS = StunScale.class;
    public static final Capability<StunScale> STUN_POINTS_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation STUN_POINTS_CAPABILITY_RL = new ResourceLocation(StatusEffects.MODID, "properties_freeze");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static StunScale getStunScaleUnwrap(LivingEntity entity) {
        return getStunScale(entity).orElse(null);
    }

    public static LazyOptional<StunScale> getStunScale(LivingEntity entity) {
        return entity.getCapability(STUN_POINTS_CAPABILITY);
    }
    private static void attacher(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new StunScale(entity), STUN_POINTS_CAPABILITY, STUN_POINTS_CAPABILITY_RL);
    }
    public static void register() {
        CapabilityAttacher.registerCapability(STUN_SCALE_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, StunScaleAttacher::attacher, StunScaleAttacher::getStunScale, true);
    }

}
