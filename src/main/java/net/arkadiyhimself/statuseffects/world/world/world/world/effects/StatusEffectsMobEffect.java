package net.arkadiyhimself.statuseffects.world.effects;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.world.effects.effectsdostuff.deafening;
import net.arkadiyhimself.statuseffects.world.effects.effectsdostuff.freeze;
import net.arkadiyhimself.statuseffects.world.effects.effectsdostuff.doomed;
import net.arkadiyhimself.statuseffects.world.effects.effectsdostuff.stunning;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class StatusEffectsMobEffect extends MobEffect {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, StatusEffects.MODID);
   public static final RegistryObject<MobEffect> STUN = EFFECTS.register("stun",
            () -> new stunning(MobEffectCategory.HARMFUL, 13859964).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    "7107DE5E-7CE8-4030-940E-514C1F160890", (double)-0.99F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> DEAFENING = EFFECTS.register("deafening",
            () -> new deafening(MobEffectCategory.HARMFUL, 1245439));

    public static final RegistryObject<MobEffect> FREEZE = EFFECTS.register("freeze",
            () -> new freeze(MobEffectCategory.HARMFUL, 8780799).addAttributeModifier(Attributes.ATTACK_SPEED,
                            "7107DE5E-7CE8-4030-940E-514C1F160890", (double)-0.8F, AttributeModifier.Operation.ADDITION).
                    addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890",
                            (double)-0.8F, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static final RegistryObject<MobEffect> DISARM = EFFECTS.register("disarm",
            () -> new StatusEffectsMobEffect(MobEffectCategory.HARMFUL, 16447222));

    public static final RegistryObject<MobEffect> DOOMED = EFFECTS.register("doomed",
            () -> new doomed(MobEffectCategory.HARMFUL, 0));



    protected StatusEffectsMobEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    public static List<Object> EFFECTS(StatusEffectsMobEffect effect) {return null;}

    @Override
    public MobEffect addAttributeModifier(Attribute attribute, String p_19474_, double number, AttributeModifier.Operation operation) {
        return super.addAttributeModifier(attribute, p_19474_, number, operation);
    }



    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
