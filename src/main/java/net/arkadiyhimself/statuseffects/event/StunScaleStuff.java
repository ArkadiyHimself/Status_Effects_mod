package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.StunScale;
import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.arkadiyhimself.statuseffects.client.AboveEntititesRenderer.StunBarType;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.sound.SwordClashSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Random;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class StunScaleStuff {

    static Random random = new Random();

    static ArrayList<DamageSource> nonStunningSource = new ArrayList<>(){{
        add(DamageSource.FREEZE);
        add(DamageSource.ON_FIRE);
        add(DamageSource.IN_FIRE);
        add(DamageSource.STARVE);
        add(DamageSource.CACTUS);
        add(DamageSource.CRAMMING);
        add(DamageSource.DRAGON_BREATH);
        add(DamageSource.DROWN);
        add(DamageSource.DRY_OUT);
        add(DamageSource.HOT_FLOOR);
        add(DamageSource.IN_WALL);
        add(DamageSource.LAVA);
        add(DamageSource.MAGIC);
        add(DamageSource.OUT_OF_WORLD);
        add(DamageSource.SWEET_BERRY_BUSH);
        add(DamageSource.WITHER);
    }};
    public static int changingRed = 128;
    public static boolean redUp;
    public static final ResourceLocation BAR_GUI = new ResourceLocation(StatusEffects.MODID, "textures/gui/statuseffects_bar_gui.png");
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(StunScale.class);
    }
    @SubscribeEvent
    public static void deathReset(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            StunScaleAttacher.getStunScale(player).ifPresent(stunScale -> {
                stunScale.setStunPoints(0);
                stunScale.setStunned(false);
                stunScale.setDecayDelay(0);
                stunScale.setCurrentDuration(0);
                stunScale.updateData();
            });
        }
    }
    @SubscribeEvent
    public static void modeChangeReset(PlayerEvent.PlayerChangeGameModeEvent event) {
        StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
            if(event.getNewGameMode() == GameType.CREATIVE || event.getNewGameMode() == GameType.SPECTATOR) {
                if(event.getEntity().hasEffect(StatusEffectsMobEffect.STUN.get())) {
                    event.getEntity().removeEffect(StatusEffectsMobEffect.STUN.get());
                }
                stunScale.setStunPoints(0);
                stunScale.setStunned(false);
                stunScale.setDecayDelay(0);
                stunScale.setCurrentDuration(0);
                stunScale.updateData();
            }
        });
    }
    @SubscribeEvent
    public static void stunPointsDecay(LivingEvent.LivingTickEvent event) {
        StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
            stunScale.subDecayDelay(1);
            if (stunScale.getDecayDelay() == 0) {
                stunScale.subStunPoints(1);
            }
            if (stunScale.isStunned()) {
                if (changingRed == 128) {
                    redUp = true;
                } else if (changingRed == 255) {
                    redUp = false;
                }
                if (redUp) {
                    changingRed = Math.min(changingRed + 8, 255);
                } else {
                    changingRed = Math.max(changingRed - 8, 128);
                }
            }
            stunScale.updateData();
        });
    }
    @SubscribeEvent
    public static void stunApplied(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {

            // updates data of StunScale capability
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                stunScale.setStunPoints(0);
                stunScale.setDecayDelay(0);
                stunScale.setStunned(true);
                stunScale.updateData();
                int duration = event.getEffectInstance().getDuration();
                stunScale.setStunDurationInitial(duration);
                stunScale.updateData();
            });

            // disables AI of stunned mob
            if(event.getEntity() instanceof Mob mob) {
                for (Goal.Flag flag : Goal.Flag.values()) {
                    mob.goalSelector.disableControlFlag(flag);
                    mob.targetSelector.disableControlFlag(flag);
                }
            }
        }
    }
    @SubscribeEvent
    public static void stunEnded(MobEffectEvent.Remove event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                stunScale.setStunned(false);
                stunScale.updateData();
            });
            if (event.getEntity() instanceof Mob mob) {
                for (Goal.Flag flag : Goal.Flag.values()) {
                    mob.goalSelector.enableControlFlag(flag);
                    mob.targetSelector.enableControlFlag(flag);
                }
            }
        }
    }
    @SubscribeEvent
    public static void pointsOnHit(LivingDamageEvent event) {
        if (!event.getEntity().hasEffect(StatusEffectsMobEffect.STUN.get()) && event.getAmount() > 0) {
            for (DamageSource damageSource : nonStunningSource) {
                if(event.getSource() == damageSource) {
                    return;
                }
            }
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                int prematureStun = (int) Math.ceil(stunScale.getStunPoints() / stunScale.getMaxStunPoints() * stunScale.getDefaultStunDurationFromHits());
                if (event.getSource() == DamageSource.FALL || event.getSource().isExplosion()) {
                    int instaStunDuration = (int) event.getAmount() * 5;
                    int finalDuration = Math.max(instaStunDuration, prematureStun);
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), Math.min(finalDuration, 150)));
                    return;
                }
                if ("mob".equals(event.getSource().getMsgId()) || "player".equals(event.getSource().getMsgId())) {
                    int newStunPoints = (int) (event.getAmount() / event.getEntity().getMaxHealth() * stunScale.getMaxStunPoints() * 2);
                    int minStunPointsAdded = (int) Math.ceil(stunScale.getMaxStunPoints() * 0.15F);

                    newStunPoints = Math.max(minStunPointsAdded, newStunPoints);
                    stunScale.addStunPoints(Math.min(stunScale.getMaxStunPointsFromHit(), newStunPoints));
                    stunScale.setDecayDelay(50);
                }
                if (stunScale.getStunPoints() == stunScale.getMaxStunPoints()) {
                    int duration = stunScale.getDefaultStunDurationFromHits();
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), duration, 1, false, false, false));
                    int num = random.nextInt(0, SwordClashSounds.amount);
                    event.getEntity().playSound(SwordClashSounds.swordClashes.get(num).getSound(), 1F, 1F);
                }
                stunScale.updateData();
            });
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public static void renderStunBarExp(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player.isCreative() || minecraft.player.isSpectator()) { return; }
        if(event.getOverlay().equals(VanillaGuiOverlay.EXPERIENCE_BAR.type())) {
            StunScaleAttacher.getStunScale(minecraft.player).ifPresent(stunScale -> {
                if (stunScale.getStunPoints() > 0 && !stunScale.isStunned()) {
                    int currentPoints = stunScale.getStunPoints();
                    int maxStunPoint = stunScale.getMaxStunPoints();
                    int stunPercent = (int) ((float) currentPoints / (float) maxStunPoint * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    int y = event.getWindow().getGuiScaledHeight() - 29;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, BAR_GUI);


                    GuiComponent.blit(poseStack, x, y, 0, 0, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 5F, stunPercent, 5, 182, 182);
                } else if (minecraft.player.hasEffect(StatusEffectsMobEffect.STUN.get())) {
                    int currentStun = stunScale.getCurrentDuration();
                    int initStun = stunScale.getStunDurationInitial();
                    int durationPercent = (int) ((float) currentStun / (float) initStun * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, BAR_GUI);
                    int y = event.getWindow().getGuiScaledHeight() - 29;
                    GuiComponent.blit(poseStack, x, y, 0, 10F, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 15F, durationPercent, 5, 182, 182);
                }
            });
        }
    }

    @SubscribeEvent
    public static void renderStunBar(RenderLivingEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        if (entity instanceof Player player) {
            if (player.isSpectator() || player.isCreative()) { return; }
        }
        Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        MultiBufferSource buffers = event.getMultiBufferSource();
        if (!entity.getPassengers().isEmpty()) {
            return;
        }

        final boolean boss = !entity.canChangeDimensions();
        poseStack.pushPose();
        final float globalScale = 0.03F;
        poseStack.translate(0, entity.getBbHeight() + 0.5, 0);
        poseStack.mulPose(cameraOrientation);
        final int light = 0xF000F0;
        poseStack.scale(-globalScale, -globalScale, -globalScale);

        VertexConsumer stunBar = buffers.getBuffer(StunBarType.BAR_TEXTURE_TYPE);

        StunScaleAttacher.getStunScale(entity).ifPresent(stunScale -> {
            if (stunScale.getStunPoints() > 0 || stunScale.isStunned()) {
                int emptyR = 255;
                int emptyG = 255;
                int emptyB = 255;
                if (!stunScale.isStunned()) {
                    // renders when entity isn't stunned yet
                    double pointsBeforeStunned = stunScale.getMaxStunPoints();
                    double currentStunPoints = stunScale.getStunPoints();
                    float currentStunPercent = (float) (currentStunPoints / pointsBeforeStunned);
                    int r = 255;
                    int g = 255;
                    int b = 255;
                    // empty bar
                    stunBar.vertex(poseStack.last().pose(), -32, 0, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32, 8, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(0.0F, 0.25F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 32, 8, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(1.0F, 0.25F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 32, 0, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
                    // filling
                    stunBar.vertex(poseStack.last().pose(), -28, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.25F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -28, 8, 0.002F).color(r, g, b, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -28 + 56 * currentStunPercent, 8, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -28 + 56 * currentStunPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 0.25F).uv2(light).endVertex();
                } else {
                    // renders when entity is stunned
                    double initDuration = stunScale.getStunDurationInitial();
                    double currentDuration = stunScale.getCurrentDuration();
                    float currentStunDurationPercent = (float) (currentDuration / initDuration);
                    int changingRed = StunScaleStuff.changingRed;
                    int r = 255;
                    int g = 255;
                    int b = 255;
                    // empty bar again
                    stunBar.vertex(poseStack.last().pose(), -32, 0, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32, 8, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(0.0F, 0.75F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 32, 8, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(1.0F, 0.75F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 32, 0, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(1.0F, 0.5F).uv2(light).endVertex();
                    // filling
                    stunBar.vertex(poseStack.last().pose(), -28, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.75F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -28, 8, 0.002F).color(r, g, b, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -28 + 56 * currentStunDurationPercent, 8, 0.002F).color(r, g, b, 255).uv(currentStunDurationPercent, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -28 + 56 * currentStunDurationPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunDurationPercent, 0.75F).uv2(light).endVertex();
                }
            }
        });
        poseStack.popPose();
    }
}
