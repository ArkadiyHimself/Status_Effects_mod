package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.FreezeEffect.FreezeEffect;
import net.arkadiyhimself.statuseffects.capability.FreezeEffect.FreezeEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffect;
import net.arkadiyhimself.statuseffects.client.AboveEntititesRenderer.SnowCrystalType;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class FreezeEffectStuff {
    public static final ResourceLocation BAR_GUI = new ResourceLocation(StatusEffects.MODID, "textures/gui/statuseffects_bar_gui.png");
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(FreezeEffect.class);
    }
    @SubscribeEvent
    public static void damageFromFreeze(LivingDamageEvent event) {
        if (event.getSource() == DamageSource.FREEZE) {
            event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.FREEZE.get(), 100, 0, false, false));
        }
    }
    @SubscribeEvent
    public static void delayingFreezeDecay(LivingEvent.LivingTickEvent event) {
        FreezeEffectAttacher.getHasFreeze(event.getEntity()).ifPresent(freezeEffect -> {
            freezeEffect.subDelayFreezeDecay(1);
            freezeEffect.updateData();
            if (freezeEffect.getDelayFreezeDecay() > 0 && !event.getEntity().isInPowderSnow) {
                event.getEntity().setTicksFrozen(Math.min(event.getEntity().getTicksFrozen() + 2, event.getEntity().getTicksRequiredToFreeze()));
            }
        });
    }
    @SubscribeEvent
    static void snowballFreeze(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult && event.getProjectile() instanceof Snowball) {
            HitResult hitresult = event.getRayTraceResult();
            Entity entity = ((EntityHitResult) hitresult).getEntity();
            int currentTicksFrozen = entity.getTicksFrozen();
            int maxTicksFrozen = entity.getTicksRequiredToFreeze();
            if (entity.canFreeze()) {
                entity.setTicksFrozen(Math.min(maxTicksFrozen, currentTicksFrozen + 30));
                if (entity.getTicksFrozen() > entity.getTicksRequiredToFreeze() * 0.8) {
                    entity.hurt(DamageSource.FREEZE, 1F);
                }
                FreezeEffectAttacher.getHasFreeze((LivingEntity) entity).ifPresent(freezeEffect -> {
                    freezeEffect.setDelayFreezeDecay(35);
                    freezeEffect.updateData();
                });
            }
        }
    }
    @SubscribeEvent
    public static void gotFrozen(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect().equals(StatusEffectsMobEffect.FREEZE.get())) {
            FreezeEffectAttacher.getHasFreeze(event.getEntity()).ifPresent(hasFreezeEffect -> {
                hasFreezeEffect.setFrozen(true);
                hasFreezeEffect.setMaxDuration(event.getEffectInstance().getDuration());
                hasFreezeEffect.updateData();
            });
        }
    }
    @SubscribeEvent
    public static void gotUnfrozen(MobEffectEvent.Remove event) {
        if(event.getEffectInstance().getEffect().equals(StatusEffectsMobEffect.FREEZE.get())) {
            FreezeEffectAttacher.getHasFreeze(event.getEntity()).ifPresent(hasFreezeEffect -> {
                hasFreezeEffect.setFrozen(false);
                hasFreezeEffect.updateData();
            });
        }
    }

    @SubscribeEvent
    public static void renderSnowCrystal(RenderLivingEvent.Pre event) {
        AtomicBoolean isDisarmed = new AtomicBoolean(false);
        DisarmEffectAttacher.getHasDisarm(event.getEntity()).ifPresent(disarmEffect -> {
            if (disarmEffect.isDisarmed()) { isDisarmed.set(true); }
        });
        if (isDisarmed.get()) { return; }
        if (event.getEntity() instanceof Player player) {
            if (player.isSpectator() || player.isCreative() || player == Minecraft.getInstance().player) { return; }
        }
        LivingEntity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        final boolean boss = !entity.canChangeDimensions();
        MultiBufferSource buffers = event.getMultiBufferSource();
        if (!entity.getPassengers().isEmpty() || boss) {
            return;
        }
        if (entity instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
        }
        poseStack.pushPose();
        final float globalScale = 0.0625F;
        Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        poseStack.translate(0, entity.getBbHeight() + 2.0F, 0);
        poseStack.mulPose(cameraOrientation);
        final int light = 0xF000F0;
        poseStack.scale(-globalScale, -globalScale, -globalScale);

        VertexConsumer snowCrystal = buffers.getBuffer(SnowCrystalType.SNOW_CRYSTAL_TYPE);
        FreezeEffectAttacher.getHasFreeze(entity).ifPresent(hasFreezeEffect -> {
            if (hasFreezeEffect.isFrozen()) {
                int alpha = (int) Math.ceil((float) hasFreezeEffect.getCurrentDuration() / (float) hasFreezeEffect.getMaxDuration() * 255);
                snowCrystal.vertex(poseStack.last().pose(), -8, 0, 0).color(255, 255, 255, alpha).uv(0.0F, 0.0F).uv2(light).endVertex();
                snowCrystal.vertex(poseStack.last().pose(), -8, 16, 0).color(255, 255, 255, alpha).uv(0.0F, 1.0F).uv2(light).endVertex();
                snowCrystal.vertex(poseStack.last().pose(), 8, 16, 0).color(255, 255, 255, alpha).uv(1.0F, 1.0F).uv2(light).endVertex();
                snowCrystal.vertex(poseStack.last().pose(), 8, 0, 0).color(255, 255, 255, alpha).uv(1.0F, 0.0F).uv2(light).endVertex();
            } else if (entity.getTicksFrozen() > 0) {
                int freezePercent = (int) (entity.getPercentFrozen() * 235);
                snowCrystal.vertex(poseStack.last().pose(), -8, 0, 0).color(255, 255, 255, 20 + freezePercent).uv(0.0F, 0.0F).uv2(light).endVertex();
                snowCrystal.vertex(poseStack.last().pose(), -8, 16, 0).color(255, 255, 255, 20 + freezePercent).uv(0.0F, 1.0F).uv2(light).endVertex();
                snowCrystal.vertex(poseStack.last().pose(), 8, 16, 0).color(255, 255, 255, 20 + freezePercent).uv(1.0F, 1.0F).uv2(light).endVertex();
                snowCrystal.vertex(poseStack.last().pose(), 8, 0, 0).color(255, 255, 255, 20 + freezePercent).uv(1.0F, 0.0F).uv2(light).endVertex();
            }
        });
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public static void renderFreezeBar(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        if (player.isCreative() || player.isSpectator()) { return; }
        if(event.getOverlay().equals(VanillaGuiOverlay.EXPERIENCE_BAR.type())) {
            FreezeEffectAttacher.getHasFreeze(minecraft.player).ifPresent(hasFreezeEffect -> {
                if (player.getTicksFrozen() > 0 && !hasFreezeEffect.isFrozen()) {
                    int freezePoints = player.getTicksFrozen();
                    int maxFreezePoints = player.getTicksRequiredToFreeze();
                    int freezePercent = (int) ((float) freezePoints / (float) maxFreezePoints * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, BAR_GUI);

                    int y = event.getWindow().getGuiScaledHeight() - 29;

                    GuiComponent.blit(poseStack, x, y, 0, 20F, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 25F, freezePercent, 5, 182, 182);
                } else if (hasFreezeEffect.isFrozen()) {
                    int currentFreeze = hasFreezeEffect.getCurrentDuration();
                    int initFreeze = hasFreezeEffect.getMaxDuration();
                    int durationPercent = (int) ((float) currentFreeze / (float) initFreeze * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, BAR_GUI);
                    int y = event.getWindow().getGuiScaledHeight() - 29;
                    GuiComponent.blit(poseStack, x, y, 0, 30F, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 35F, durationPercent, 5, 182, 182);
                }
            });
        }
    }
}
