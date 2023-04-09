package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.arkadiyhimself.statuseffects.networking.packets.RingingInEarsS2CPacket;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class StatusEffectsEventHandler {
    @SubscribeEvent
    public static void damageEffectsApplying(LivingDamageEvent event) {
        if ("sonic_boom".equals(event.getSource().getMsgId())) {
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.DEAFENING.get(), 200, 1, false, false, false));
            if (event.getEntity() instanceof Player) {
                Entity entity = event.getEntity();
                NetworkHandler.sentToPlayer(new RingingInEarsS2CPacket(), (ServerPlayer) entity);
            }
        }
        if (event.getSource().isExplosion() && event.getAmount() > 4) {
            int i = (int) event.getAmount() * 10;
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.DEAFENING.get(), Math.min(i, 300), 1, false, false));
            if (event.getEntity() instanceof Player) {
                Entity entity = event.getEntity();
                NetworkHandler.sentToPlayer(new RingingInEarsS2CPacket(), (ServerPlayer) entity);
            }

        }
    }
    @SubscribeEvent
    static void muteSounds(PlaySoundEvent event) {
        boolean exception = false;
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
}
