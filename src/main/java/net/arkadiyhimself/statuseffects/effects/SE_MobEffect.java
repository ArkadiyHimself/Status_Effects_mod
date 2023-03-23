package net.arkadiyhimself.statuseffects.effects;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.arkadiyhimself.statuseffects.effects.effectsdostuff.deafening;
import net.arkadiyhimself.statuseffects.effects.effectsdostuff.freeze;
import net.arkadiyhimself.statuseffects.effects.effectsdostuff.doomed;
import net.arkadiyhimself.statuseffects.effects.effectsdostuff.stunning;
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

public class SE_MobEffect extends MobEffect {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Status_Effects.MODID);
   public static final RegistryObject<MobEffect> STUN = EFFECTS.register("stun",
            () -> new stunning(MobEffectCategory.HARMFUL, 13859964).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    "7107DE5E-7CE8-4030-940E-514C1F160890", (double)-0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> DEAFENING = EFFECTS.register("deafening",
            () -> new deafening(MobEffectCategory.HARMFUL, 1245439));

    public static final RegistryObject<MobEffect> FREEZE = EFFECTS.register("freeze",
            () -> new freeze(MobEffectCategory.HARMFUL, 8780799).addAttributeModifier(Attributes.ATTACK_SPEED,
                            "7107DE5E-7CE8-4030-940E-514C1F160890", (double)-0.8F, AttributeModifier.Operation.ADDITION).
                    addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890",
                            (double)-0.8F, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static final RegistryObject<MobEffect> DISARM = EFFECTS.register("disarm",
            () -> new SE_MobEffect(MobEffectCategory.HARMFUL, 16447222));

    public static final RegistryObject<MobEffect> DOOMED = EFFECTS.register("doomed",
            () -> new doomed(MobEffectCategory.HARMFUL, 0));



    protected SE_MobEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    public static List<Object> EFFECTS(SE_MobEffect effect) {return null;}

    @Override
    public MobEffect addAttributeModifier(Attribute attribute, String p_19474_, double number, AttributeModifier.Operation operation) {
        return super.addAttributeModifier(attribute, p_19474_, number, operation);
    }



    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
