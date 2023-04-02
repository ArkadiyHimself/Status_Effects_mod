package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.networking.Messages;
import net.arkadiyhimself.statuseffects.networking.packets.DoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.networking.packets.RingingInEarsS2CPacket;
import net.arkadiyhimself.statuseffects.world.effects.StatusEffectsMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.goal.Goal;
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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class StatusEffectsEventHandler {

    @SubscribeEvent
    public static void damageEffectsApplying(LivingDamageEvent event) {
        if (event.getSource() == DamageSource.FALL) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), Math.min(i, 150)));
        }

        if ("sonic_boom".equals(event.getSource().getMsgId())) {
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.DEAFENING.get(), 200));
            if (event.getEntity() instanceof Player) {
//                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.RINGING_LONG.get(), 1.0F, 5.0F));
                Entity entity = event.getEntity();
                Messages.sentToPlayer(new RingingInEarsS2CPacket(), (ServerPlayer) entity);
            }
        }

        if (event.getSource().isExplosion()) {
            int i = (int) Math.ceil(event.getAmount() * 5);
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), Math.min(i, 150)));
            if (event.getAmount() > 4) {
                event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.DEAFENING.get(), Math.min(i, 150) * 2));
                if (event.getEntity() instanceof Player) {
                    Entity entity = event.getEntity();
//                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.RINGING_LONG.get(), 1.0F, 5.0F));
                    Messages.sentToPlayer(new RingingInEarsS2CPacket(), (ServerPlayer) entity);
                }
            }
        }

        if (event.getSource() == DamageSource.FREEZE) {
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.FREEZE.get(), 100));
        }
        if (event.getEntity().hasEffect(StatusEffectsMobEffect.DOOMED.get()) && event.getAmount() > 0) {
            if (event.getEntity().getMaxHealth() <= 100) {
                event.getEntity().removeEffect(StatusEffectsMobEffect.DOOMED.get());
                event.setAmount(Float.MAX_VALUE);
            } else {
                event.setAmount(event.getAmount() * 2);
            }
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
        if (event.isCancelable() && Minecraft.getInstance().level != null && player.hasEffect(StatusEffectsMobEffect.DEAFENING.get()) && !exception) {
            event.setSound(null);
        } else if (Minecraft.getInstance().level != null && player.hasEffect(StatusEffectsMobEffect.DEAFENING.get())) {

        }
    }

    @SubscribeEvent
    static void disarmPlayer(AttackEntityEvent event) {
        if (event.isCancelable()) {
            if (event.getEntity().hasEffect(StatusEffectsMobEffect.DISARM.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void disarmMobs(LivingAttackEvent event) {
        if ("mob".equals(event.getSource().getMsgId())) {
            DamageSource source = event.getSource();
            Entity entity = ((EntityDamageSource) source).getEntity();
            if (entity instanceof Mob mob) {
                event.setCanceled(mob.hasEffect(StatusEffectsMobEffect.DISARM.get()) || mob.hasEffect(StatusEffectsMobEffect.STUN.get()));
            }
        }
    }

    @SubscribeEvent
    static void snowballFreeze(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult && event.getProjectile() instanceof Snowball) {
            HitResult hitresult = event.getRayTraceResult();
            Entity entity = ((EntityHitResult) hitresult).getEntity();
            ((LivingEntity) entity).addEffect(new MobEffectInstance(StatusEffectsMobEffect.FREEZE.get(), 30));
        }
    }

    @SubscribeEvent
    static void stunMouseInputs(InputEvent.InteractionKeyMappingTriggered event) {
        if (Minecraft.getInstance().level != null) {
            assert Minecraft.getInstance().player != null;
            Player player = Minecraft.getInstance().player;
            event.setCanceled(player.hasEffect(StatusEffectsMobEffect.STUN.get()));
            event.setSwingHand(!player.hasEffect(StatusEffectsMobEffect.STUN.get()) && !player.hasEffect(StatusEffectsMobEffect.DISARM.get()));
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    static void effectWasApplied(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DOOMED.get() && event.getEntity() instanceof Player) {
//            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.DOOMED.get(), 1.0F, 1.0F));
            Entity entity = event.getEntity();
            Messages.sentToPlayer(new DoomedSoundS2CPacket(), (ServerPlayer) entity);
        }
        if (event.getEntity() instanceof Warden && event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DISARM.get()) {
            SonicBoom.setCooldown(event.getEntity(), 0);
        }
        if (event.getEntity() instanceof Mob mob && event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {
            for (Goal.Flag flag : Goal.Flag.values()) {
                mob.goalSelector.disableControlFlag(flag);
                mob.targetSelector.disableControlFlag(flag);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    static void effectEnded(MobEffectEvent.Remove event) {
        if (event.getEntity() instanceof Mob mob && event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {
            for (Goal.Flag flag : Goal.Flag.values()) {
                mob.goalSelector.enableControlFlag(flag);
                mob.targetSelector.enableControlFlag(flag);
            }
        }
    }

}
