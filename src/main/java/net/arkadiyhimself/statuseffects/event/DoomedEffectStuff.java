package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.arkadiyhimself.statuseffects.networking.packets.DoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class DoomedEffectStuff {
    private static boolean isDoomed(LivingEntity entity) { return entity.hasEffect(StatusEffectsMobEffect.DOOMED.get()); }
    @SubscribeEvent
    public static void tookDamage(LivingDamageEvent event) {
        if (event.getEntity().hasEffect(StatusEffectsMobEffect.DOOMED.get()) && event.getEntity().getMaxHealth() <= 100) {
            LivingEntity entity = event.getEntity();
            event.setAmount(Float.MAX_VALUE);
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            entity.level.addParticle(StatusEffectsParticles.FALLEN_SOUL.get(), x, y, z, 0, 0, 0);
            entity.playSound(StatusEffectsSounds.FALLEN_BREATH.getSound(),1F,1F);
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(StatusEffectsSounds.FALLEN_BREATH.getSound(),1F,1F));
        }
    }

    // this one works fine
    @SubscribeEvent
    public static void doomedApplied(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DOOMED.get() && event.getEntity() instanceof Player) {
            Entity entity = event.getEntity();
            NetworkHandler.sentToPlayer(new DoomedSoundS2CPacket(), (ServerPlayer) entity);
        }
    }


    // don't pay attention to this one, I have specific plans on it
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public static void renderSkullHearts(RenderGuiOverlayEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.getOverlay().equals(VanillaGuiOverlay.PLAYER_HEALTH.type()) && isDoomed(player)) {
            event.setCanceled(true);
            Gui gui = Minecraft.getInstance().gui;

        }
    }
}
