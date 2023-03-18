package net.arkadiyhimself.statuseffects;

import net.arkadiyhimself.statuseffects.effects.ModMobEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Status_Effects.MODID)
public class ModEventHandler {

    @SubscribeEvent
    static void naturaleffects(LivingDamageEvent event) {
        if(event.getSource() == DamageSource.FALL) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.STUN.get(), Math.min(i, 150)));
        }

        if("sonic_boom".equals(event.getSource().getMsgId())) {
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.DEAFENING.get(), 20));
        }

        if(event.getSource().isExplosion()) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.STUN.get(), Math.min(i, 150)));
            if(event.getAmount() > 4) {
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.DEAFENING.get(), Math.min(i, 150)));
            }
        }

        if(event.getSource() == DamageSource.FREEZE) {
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.FREEZE.get(), 100));
        }
    }

}