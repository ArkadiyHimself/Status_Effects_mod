package net.arkadiyhimself.statuseffects.capability.DisarmEffect;

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

public class DisarmEffectAttacher extends CapabilityAttacher {
    private static final Class<DisarmEffect> HAS_DISARM_EFFECT_CLASS = DisarmEffect.class;
    public static final Capability<DisarmEffect> HAS_DISARM_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation HAS_DISARM_CAPABILITY_RL = new ResourceLocation(StatusEffects.MODID, "properties_disarm");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static DisarmEffect getHasDisarmUnwrap(LivingEntity entity) {
        return getHasDisarm(entity).orElse(null);
    }

    public static LazyOptional<DisarmEffect> getHasDisarm(LivingEntity entity) {
        return entity.getCapability(HAS_DISARM_CAPABILITY);
    }
    private static void attacher(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new DisarmEffect(entity), HAS_DISARM_CAPABILITY, HAS_DISARM_CAPABILITY_RL);
    }
    public static void register() {
        CapabilityAttacher.registerCapability(HAS_DISARM_EFFECT_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, DisarmEffectAttacher::attacher, DisarmEffectAttacher::getHasDisarm, true);
    }

}
