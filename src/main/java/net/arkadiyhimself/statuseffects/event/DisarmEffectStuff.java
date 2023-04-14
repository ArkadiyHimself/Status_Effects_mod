package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffect;
import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffect;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffectAttacher;
import net.arkadiyhimself.statuseffects.client.AboveEntititesRenderer.DisarmedSwordType;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class DisarmEffectStuff {
    static int deniedSoundCooldown = 20;
    static double iconHeight = 5F;
    static boolean goUp;
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(DisarmEffect.class);
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
            Entity entity = (source).getEntity();
            if (entity instanceof Mob mob) {
                event.setCanceled(mob.hasEffect(StatusEffectsMobEffect.DISARM.get()) || mob.hasEffect(StatusEffectsMobEffect.STUN.get()));
            }
        }
    }
    @SubscribeEvent
    public static void changeHeightOfIcon(LivingEvent.LivingTickEvent event) {
        // update the direction in which the icon is going
        if (iconHeight == 0) { goUp = true; } else if (iconHeight == 5F) { goUp = false; }
        // moves the icon in respective direction, causing it to go uo and down
        if (goUp) { iconHeight = Math.min(5F, iconHeight + 0.02F); } else { iconHeight = Math.max(0, iconHeight - 0.02F); }
        if (deniedSoundCooldown > 0) { deniedSoundCooldown -= 1; }
    }
    @SubscribeEvent
    public static void disarmApplied(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DISARM.get()) {
            DisarmEffectAttacher.getHasDisarm(event.getEntity()).ifPresent(disarmEffect -> {
                disarmEffect.setDisarmed(true);
                disarmEffect.updateTracking();
            });
            if (event.getEntity() instanceof Warden) {
                SonicBoom.setCooldown(event.getEntity(), 0);
            }
        }
    }
    @SubscribeEvent
    public static void cancelSwing(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isAttack() && Minecraft.getInstance().player.hasEffect(StatusEffectsMobEffect.DISARM.get())) {
            event.setCanceled(true);
            event.setSwingHand(false);
            if (deniedSoundCooldown == 0) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(StatusEffectsSounds.ATTACK_DENIED.get(), 1F, 1F));
                deniedSoundCooldown = 20;
            }
        }
    }
    @SubscribeEvent
    public static void disarmEnded(MobEffectEvent.Remove event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DISARM.get()) {
            DisarmEffectAttacher.getHasDisarm(event.getEntity()).ifPresent(disarmEffect -> {
                disarmEffect.setDisarmed(false);
                disarmEffect.updateTracking();
            });
        }
    }
    @SubscribeEvent
    public static void renderDisarmedThirdPerson(RenderLivingEvent.Pre event) {
        if (event.getEntity().equals(Minecraft.getInstance().player) || !event.getEntity().getPassengers().isEmpty()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        MultiBufferSource buffers = event.getMultiBufferSource();
        poseStack.pushPose();
        final float globalScale = 0.0625F;
        StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
            if (stunEffect.isStunned() || stunEffect.getStunPoints() > 0) { poseStack.translate(0, entity.getBbHeight() + 2.5F, 0); }
            else { poseStack.translate(0, entity.getBbHeight() + 2.0F, 0); }
        });
        poseStack.mulPose(cameraOrientation);
        final int light = 0xF000F0;
        poseStack.scale(-globalScale, -globalScale, -globalScale);

        VertexConsumer brokenSword = buffers.getBuffer(DisarmedSwordType.BROKEN_SWORD_TYPE);

        DisarmEffectAttacher.getHasDisarm(entity).ifPresent(disarmEffect -> {
            if (disarmEffect.isDisarmed()) {
                brokenSword.vertex(poseStack.last().pose(), -10.0F, (float) iconHeight, 0).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
                brokenSword.vertex(poseStack.last().pose(), -10.0F, 20 + (float) iconHeight, 0).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
                brokenSword.vertex(poseStack.last().pose(), 10.0F, 20 + (float) iconHeight, 0).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
                brokenSword.vertex(poseStack.last().pose(), 10.0F, (float) iconHeight, 0).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
            }
        });
        poseStack.popPose();
    }
}
