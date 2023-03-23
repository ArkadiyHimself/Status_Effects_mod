package net.arkadiyhimself.statuseffects;

import com.mojang.blaze3d.audio.Library;
import net.arkadiyhimself.statuseffects.effects.SE_MobEffect;
import net.arkadiyhimself.statuseffects.sound.SE_Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Status_Effects.MODID)
public class SE_EventHandler {

    @SubscribeEvent
    public static void damageEffectsApplying(LivingDamageEvent event) {

        if (event.getSource() == DamageSource.FALL) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(SE_MobEffect.STUN.get(), Math.min(i, 150)));
        }

        if ("sonic_boom".equals(event.getSource().getMsgId())) {
            event.getEntity().addEffect(new MobEffectInstance(SE_MobEffect.DEAFENING.get(), 200));
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.RINGING_LONG.get(), 1.0F, 5.0F));
        }

        if (event.getSource().isExplosion()) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(SE_MobEffect.STUN.get(), Math.min(i, 150)));
            if (event.getAmount() > 4) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.RINGING_LONG.get(), 1.0F, 5.0F));
                event.getEntity().addEffect(new MobEffectInstance(SE_MobEffect.DEAFENING.get(), Math.min(i, 150) * 2));
            }
        }

        if (event.getSource() == DamageSource.FREEZE) {
            event.getEntity().addEffect(new MobEffectInstance(SE_MobEffect.FREEZE.get(), 100));
        }
        if (event.getEntity().hasEffect(SE_MobEffect.DOOMED.get())) {
            event.getEntity().removeEffect(SE_MobEffect.DOOMED.get());
            if(event.isCancelable()) { event.setCanceled(true); }
            event.getEntity().kill();
        }
    }


    @SubscribeEvent
    static void muteSounds(PlaySoundEvent event) {
        boolean exception = false;
        Sound sound = Objects.requireNonNull(event.getSound()).getSound();
        if (event.getSound() instanceof SimpleSoundInstance) {
            exception = event.getName().equals("ringing_long") || event.getName().equals("entity.warden.sonic_boom")
                    || event.getName().equals("entity.generic.explode") || event.getName().equals("ui.toast.challenge_complete")
                    || event.getName().equals("doomed") || event.getName().equals("undoomed");
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.isCancelable() &&  Minecraft.getInstance().level != null && player.hasEffect(SE_MobEffect.DEAFENING.get()) && !exception) {
            event.setSound(null);
        } else if (Minecraft.getInstance().level != null && player.hasEffect(SE_MobEffect.DEAFENING.get())) {

        }
    }
    @SubscribeEvent
    static void disarmPlayer(AttackEntityEvent event) {
        if(event.isCancelable()) {
            if(event.getEntity().hasEffect(SE_MobEffect.DISARM.get())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    static void disarmMobs(LivingAttackEvent event) {
        if(event.getSource() instanceof EntityDamageSource) {
            DamageSource source = event.getSource();
            Entity entity = ((EntityDamageSource)source).getEntity();
            if(((LivingEntity)entity).hasEffect(SE_MobEffect.DISARM.get())) {
                event.setCanceled(true); }
        }
    }

    @SubscribeEvent
    static void snowballFreeze(ProjectileImpactEvent event) {
        if(event.getRayTraceResult() instanceof EntityHitResult && event.getProjectile() instanceof Snowball) {
            HitResult hitresult = event.getRayTraceResult();
            Entity entity = ((EntityHitResult)hitresult).getEntity();
            ((LivingEntity)entity).addEffect(new MobEffectInstance(SE_MobEffect.FREEZE.get(), 30));
        }
    }



    @SubscribeEvent
    static void stunPlayer(InputEvent.MouseButton event) {
        if(Minecraft.getInstance().level != null) {
            if(Minecraft.getInstance().player.hasEffect(SE_MobEffect.STUN.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void effectWasApplied(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect() == SE_MobEffect.DOOMED.get() && event.getEntity() instanceof Player) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.DOOMED.get(), 1.0F, 5.0F));
        }
        if(event.getEntity() instanceof Warden && event.getEffectInstance().getEffect() == SE_MobEffect.DISARM.get()) {
            SonicBoom.setCooldown(event.getEntity(), 0);
        }
    }

    @SubscribeEvent
    static void effectEnded(MobEffectEvent event) {
    }
}