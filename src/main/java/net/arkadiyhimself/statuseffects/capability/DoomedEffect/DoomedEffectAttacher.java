package net.arkadiyhimself.statuseffects.capability.DoomedEffect;

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

public class DoomedEffectAttacher extends CapabilityAttacher {
    private static final Class<DoomedEffect> HAS_DOOMED_EFFECT_CLASS = DoomedEffect.class;
    public static final Capability<DoomedEffect> HAS_DOOMED_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation HAS_DOOMED_CAPABILITY_RL = new ResourceLocation(StatusEffects.MODID, "properties_doomed");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static DoomedEffect getHasDoomedUnwrap(LivingEntity entity) {
        return getHasDoomed(entity).orElse(null);
    }

    public static LazyOptional<DoomedEffect> getHasDoomed(LivingEntity entity) {
        return entity.getCapability(HAS_DOOMED_CAPABILITY);
    }
    private static void attacher(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new DoomedEffect(entity), HAS_DOOMED_CAPABILITY, HAS_DOOMED_CAPABILITY_RL);
    }
    public static void register() {
        CapabilityAttacher.registerCapability(HAS_DOOMED_EFFECT_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, DoomedEffectAttacher::attacher, DoomedEffectAttacher::getHasDoomed, true);
    }

}
