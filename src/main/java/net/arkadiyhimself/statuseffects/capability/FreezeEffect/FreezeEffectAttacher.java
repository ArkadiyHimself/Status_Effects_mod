package net.arkadiyhimself.statuseffects.capability.FreezeEffect;

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

public class FreezeEffectAttacher extends CapabilityAttacher {
    private static final Class<FreezeEffect> HAS_FREEZE_EFFECT_CLASS = FreezeEffect.class;
    public static final Capability<FreezeEffect> HAS_FREEZE_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation HAS_FREEZE_CAPABILITY_RL = new ResourceLocation(StatusEffects.MODID, "properties_stun");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static FreezeEffect getHasFreezeUnwrap(LivingEntity entity) {
        return getHasFreeze(entity).orElse(null);
    }

    public static LazyOptional<FreezeEffect> getHasFreeze(LivingEntity entity) {
        return entity.getCapability(HAS_FREEZE_CAPABILITY);
    }
    private static void attacher(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new FreezeEffect(entity), HAS_FREEZE_CAPABILITY, HAS_FREEZE_CAPABILITY_RL);
    }
    public static void register() {
        CapabilityAttacher.registerCapability(HAS_FREEZE_EFFECT_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, FreezeEffectAttacher::attacher, FreezeEffectAttacher::getHasFreeze, true);
    }

}
