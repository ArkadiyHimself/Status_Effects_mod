package net.arkadiyhimself.statuseffects.effects;

import com.google.common.collect.Maps;
import net.arkadiyhimself.statuseffects.Status_Effects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModMobEffects extends MobEffect {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Status_Effects.MODID);
    public static final RegistryObject<MobEffect> STUN = EFFECTS.register("stun",
            () -> new ModMobEffects(MobEffectCategory.HARMFUL, 3486368).
                    addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890",
                            (double)-0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL).
                    addAttributeModifier(Attributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9",
                            (double)-0.00F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> DEAFENING = EFFECTS.register("deafening",
            () -> new ModMobEffects(MobEffectCategory.HARMFUL, 4389348));



    protected ModMobEffects(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        super.applyEffectTick(p_19467_, p_19468_);
    }

    @Override
    public MobEffect addAttributeModifier(Attribute p_19473_, String p_19474_, double p_19475_, AttributeModifier.Operation p_19476_) {
        return super.addAttributeModifier(p_19473_, p_19474_, p_19475_, p_19476_);
    }

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
