package net.arkadiyhimself.statuseffects.event;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.arkadiyhimself.statuseffects.networking.packets.RingingInEarsS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class DeafeningEffectStuff {
    public static List<String> unMutedSounds = new ArrayList<>() {{
        add("ringing_long");
        add("entity.warden.sonic_boom");
        add("entity.generic.explode");
        add("ui.toast.challenge_complete");
        add("doomed");
        add("undoomed");
    }};
    @SubscribeEvent
    public static void damageEffectsApplying(LivingDamageEvent event) {
        if ("sonic_boom".equals(event.getSource().getMsgId())) {
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.DEAFENING.get(), 200, 1, false, false));
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
        for (String sound : unMutedSounds) {
            if (event.getName().equals(sound)) { return; }
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.isCancelable() && Minecraft.getInstance().level != null && player.hasEffect(StatusEffectsMobEffect.DEAFENING.get())) {
            event.setSound(null);
        }
    }
}
