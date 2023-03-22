package net.arkadiyhimself.statuseffects;

import net.arkadiyhimself.statuseffects.effects.ModMobEffect;
import net.arkadiyhimself.statuseffects.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
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
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Status_Effects.MODID)
public class ModEventHandler {
    public boolean deaf = false;

    @SubscribeEvent
    static void damageEffectsApplying(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (event.getSource() == DamageSource.FALL) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.STUN.get(), Math.min(i, 150)));
        }

        if ("sonic_boom".equals(event.getSource().getMsgId())) {
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.DEAFENING.get(), 200));
            event.getEntity().level.playSound(Minecraft.getInstance().player, x, y, z, ModSounds.RINGING_LONG.get(), SoundSource.PLAYERS, 5.0F, 10.0F);

        }

        if (event.getSource().isExplosion()) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.STUN.get(), Math.min(i, 150)));
            if (event.getAmount() > 4) {
                event.getEntity().level.playSound(Minecraft.getInstance().player, x, y, z, ModSounds.RINGING_LONG.get(), SoundSource.PLAYERS, 5.0F, 10.0F);
                event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.DEAFENING.get(), Math.min(i, 150) * 2));
            }
        }

        if (event.getSource() == DamageSource.FREEZE) {
            event.getEntity().addEffect(new MobEffectInstance(ModMobEffect.FREEZE.get(), 100));
        }
        if (event.getEntity().hasEffect(ModMobEffect.DOOMED.get())) {
            event.getEntity().removeEffect(ModMobEffect.DOOMED.get());
            if(event.isCancelable()) { event.setCanceled(true); }
            event.getEntity().kill();
        }
    }


    @SubscribeEvent
    static void muteSounds(PlaySoundEvent event) {
        boolean exception = false;
        if (event.getSound() instanceof SimpleSoundInstance) {
            exception = event.getName().equals("ringing_long") || event.getName().equals("entity.warden.sonic_boom")
                    || event.getName().equals("entity.generic.explode");
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.isCancelable() &&  Minecraft.getInstance().level != null && player.hasEffect(ModMobEffect.DEAFENING.get()) && !exception) {
            event.setSound(null);
        }
    }
    @SubscribeEvent
    static void disarmPlayer(LivingAttackEvent event) {
        if(event.isCancelable()) {
            if(event.getEntity().hasEffect(ModMobEffect.DISARM.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void snowballFreeze(ProjectileImpactEvent event) {
        if(event.getRayTraceResult().getType() == HitResult.Type.ENTITY && event.getProjectile() instanceof Snowball) {
//            LivingEntity entity = (LivingEntity) event.getEntity();
//            entity.addEffect(new MobEffectInstance(ModMobEffect.FREEZE.get(), 2));
        }
    }



    @SubscribeEvent
    static void stunPlayer(InputEvent.MouseButton event) {
        if(Minecraft.getInstance().level != null) {
            if(Minecraft.getInstance().player.hasEffect(ModMobEffect.STUN.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void effectWasApplied(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if(event.getEffectInstance().getEffect() == ModMobEffect.DOOMED.get()) {
            event.getEntity().level.playSound(Minecraft.getInstance().player, x, y, z, ModSounds.DOOMED.get(), SoundSource.PLAYERS, 5.0F, 1.0F);
        }
    }

    @SubscribeEvent
    static void effectEnded(MobEffectEvent event) {
    }
}