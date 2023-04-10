package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffect;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffectAttacher;
import net.arkadiyhimself.statuseffects.client.AboveEntititesRenderer.StunBarType;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.sound.SwordClashSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
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
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;
import team.creative.creativecore.common.gui.event.GuiEvent;
import team.creative.creativecore.common.gui.event.GuiEventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class StunEffectStuff {

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
        event.register(StunEffect.class);
    }
    @SubscribeEvent
    public static void deathReset(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            StunEffectAttacher.getStunEffect(player).ifPresent(stunEffect -> {
                stunEffect.setStunPoints(0);
                stunEffect.setStunned(false);
                stunEffect.setDecayDelay(0);
                stunEffect.setCurrentDuration(0);
                stunEffect.updateData();
            });
        }
    }
    public static List<Class<? extends Screen>> cancelledGui = new ArrayList<>() {{
        add(BookEditScreen.class);
        add(BookViewScreen.class);
        add(AbstractContainerScreen.class);
        add(HorseInventoryScreen.class);
        add(InventoryScreen.class);
    }};
    @SubscribeEvent
    public static void cancelGui(ScreenEvent.Opening event) {
        if (Minecraft.getInstance().level != null) {
            if (Minecraft.getInstance().player.hasEffect(StatusEffectsMobEffect.STUN.get())) {
                for (Class screen : cancelledGui) {
                    if (event.getNewScreen().getClass() == screen) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void modeChangeReset(PlayerEvent.PlayerChangeGameModeEvent event) {
        StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
            if(event.getNewGameMode() == GameType.CREATIVE || event.getNewGameMode() == GameType.SPECTATOR) {
                if(event.getEntity().hasEffect(StatusEffectsMobEffect.STUN.get())) {
                    event.getEntity().removeEffect(StatusEffectsMobEffect.STUN.get());
                }
                stunEffect.setStunPoints(0);
                stunEffect.setStunned(false);
                stunEffect.setDecayDelay(0);
                stunEffect.setCurrentDuration(0);
                stunEffect.updateData();
            }
        });
    }
    @SubscribeEvent
    public static void stunPointsDecay(LivingEvent.LivingTickEvent event) {
        StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
            stunEffect.subDecayDelay(1);
            if (stunEffect.getDecayDelay() == 0) {
                stunEffect.subStunPoints(1);
            }
            if (stunEffect.isStunned()) {
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
            stunEffect.updateData();
        });
    }
    @SubscribeEvent
    public static void stunApplied(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {

            // updates data of StunScale capability
            StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
                stunEffect.setStunPoints(0);
                stunEffect.setDecayDelay(0);
                stunEffect.setStunned(true);
                stunEffect.updateData();
                int duration = event.getEffectInstance().getDuration();
                stunEffect.setStunDurationInitial(duration);
                stunEffect.updateData();
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
            StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
                stunEffect.setStunned(false);
                stunEffect.updateData();
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
            StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
                int prematureStun = (int) Math.ceil(stunEffect.getStunPoints() / stunEffect.getMaxStunPoints() * stunEffect.getDefaultStunDurationFromHits());
                if (event.getSource() == DamageSource.FALL || event.getSource().isExplosion()) {
                    int instaStunDuration = (int) event.getAmount() * 5;
                    int finalDuration = Math.max(instaStunDuration, prematureStun);
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), Math.min(finalDuration, 150)));
                    return;
                }
                if ("mob".equals(event.getSource().getMsgId()) || "player".equals(event.getSource().getMsgId())) {
                    int newStunPoints = (int) (event.getAmount() / event.getEntity().getMaxHealth() * stunEffect.getMaxStunPoints() * 2);
                    int minStunPointsAdded = (int) Math.ceil(stunEffect.getMaxStunPoints() * 0.15F);

                    newStunPoints = Math.max(minStunPointsAdded, newStunPoints);
                    stunEffect.addStunPoints(Math.min(stunEffect.getMaxStunPointsFromHit(), newStunPoints));
                    stunEffect.setDecayDelay(50);
                }
                if (stunEffect.getStunPoints() == stunEffect.getMaxStunPoints()) {
                    int duration = stunEffect.getDefaultStunDurationFromHits();
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), duration, 1, false, false, false));
                    int num = random.nextInt(0, SwordClashSounds.amount);
                    event.getEntity().playSound(SwordClashSounds.swordClashes.get(num).getSound(), 1F, 1F);
                }
                stunEffect.updateData();
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

    @SubscribeEvent
    public static void stunWithShield(ShieldBlockEvent event) {
        DamageSource source = event.getDamageSource();
        double healthBlocker = event.getEntity().getMaxHealth();
        double amount = event.getOriginalBlockedDamage();
        StunEffectAttacher.getStunEffect(event.getEntity()).ifPresent(stunEffect -> {
            int stunPoints = (int) Math.ceil(amount / healthBlocker * 5);
            stunEffect.addStunPoints(stunPoints);
            stunEffect.setDecayDelay(Math.min(stunEffect.getDecayDelay(), 30));
            stunEffect.updateData();
            if (stunEffect.getStunPoints() == stunEffect.getMaxStunPoints()) {
                event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), stunEffect.getDefaultStunDurationFromHits(), 1, false, false));
            }
        });
        if (source.getEntity() instanceof LivingEntity entity) {
            double healthAttacker = entity.getMaxHealth();
            StunEffectAttacher.getStunEffect(entity).ifPresent(stunEffect -> {
                int stunPoints = (int) Math.ceil(amount / healthAttacker * stunEffect.getMaxStunPoints());
                stunEffect.addStunPoints(stunPoints);
                stunEffect.setDecayDelay(Math.min(stunEffect.getDecayDelay(), 30));
                stunEffect.updateData();
                if (stunEffect.getStunPoints() == stunEffect.getMaxStunPoints()) {
                    entity.addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), stunEffect.getDefaultStunDurationFromHits(), 1, false, false));
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public static void renderStunBarExp(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player.isCreative() || minecraft.player.isSpectator()) { return; }
        if(event.getOverlay().equals(VanillaGuiOverlay.EXPERIENCE_BAR.type())) {
            StunEffectAttacher.getStunEffect(minecraft.player).ifPresent(stunEffect -> {
                if (stunEffect.getStunPoints() > 0 && !stunEffect.isStunned()) {
                    int currentPoints = stunEffect.getStunPoints();
                    int maxStunPoint = stunEffect.getMaxStunPoints();
                    int stunPercent = (int) ((float) currentPoints / (float) maxStunPoint * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    int y = event.getWindow().getGuiScaledHeight() - 29;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, BAR_GUI);


                    GuiComponent.blit(poseStack, x, y, 0, 0, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 5F, stunPercent, 5, 182, 182);
                } else if (minecraft.player.hasEffect(StatusEffectsMobEffect.STUN.get())) {
                    int currentStun = stunEffect.getCurrentDuration();
                    int initStun = stunEffect.getStunDurationInitial();
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
            if (player.isSpectator() || player.isCreative() || player == Minecraft.getInstance().player) { return; }
        }
        Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        MultiBufferSource buffers = event.getMultiBufferSource();
        if (!entity.getPassengers().isEmpty()) {
            return;
        }

        final boolean boss = !entity.canChangeDimensions();
        poseStack.pushPose();
        final float globalScale = 0.0625F;
        poseStack.translate(0, entity.getBbHeight() + 0.75, 0);
        poseStack.mulPose(cameraOrientation);
        final int light = 0xF000F0;
        poseStack.scale(-globalScale, -globalScale, -globalScale);

        VertexConsumer stunBar = buffers.getBuffer(StunBarType.BAR_TEXTURE_TYPE);

        StunEffectAttacher.getStunEffect(entity).ifPresent(stunEffect -> {
            if (stunEffect.getStunPoints() > 0 || stunEffect.isStunned()) {
                int emptyR = 255;
                int emptyG = 255;
                int emptyB = 255;
                if (!stunEffect.isStunned()) {
                    // renders when entity isn't stunned yet
                    double pointsBeforeStunned = stunEffect.getMaxStunPoints();
                    double currentStunPoints = stunEffect.getStunPoints();
                    float currentStunPercent = (float) (currentStunPoints / pointsBeforeStunned);
                    int r = 255;
                    int g = 255;
                    int b = 255;
                    // empty bar
                    stunBar.vertex(poseStack.last().pose(), -16, 0, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -16, 4, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(0.0F, 0.25F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 16, 4, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(1.0F, 0.25F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 16, 0, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
                    // filling
                    stunBar.vertex(poseStack.last().pose(), -14, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.25F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -14, 4, 0.002F).color(r, g, b, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -14 + 28 * currentStunPercent, 4, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -14 + 28 * currentStunPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 0.25F).uv2(light).endVertex();
                } else {
                    // renders when entity is stunned
                    double initDuration = stunEffect.getStunDurationInitial();
                    double currentDuration = stunEffect.getCurrentDuration();
                    float currentStunDurationPercent = (float) (currentDuration / initDuration);
                    int changingRed = StunEffectStuff.changingRed;
                    int r = 255;
                    int g = 255;
                    int b = 255;
                    // empty bar again
                    stunBar.vertex(poseStack.last().pose(), -16, 0, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -16, 4, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(0.0F, 0.75F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 16, 4, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(1.0F, 0.75F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), 16, 0, 0.001F).color(changingRed, emptyG, emptyB, 255).uv(1.0F, 0.5F).uv2(light).endVertex();
                    // filling
                    stunBar.vertex(poseStack.last().pose(), -14, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.75F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -14, 4, 0.002F).color(r, g, b, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -14 + 28 * currentStunDurationPercent, 4, 0.002F).color(r, g, b, 255).uv(currentStunDurationPercent, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -14 + 28 * currentStunDurationPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunDurationPercent, 0.75F).uv2(light).endVertex();
                }
            }
        });
        poseStack.popPose();
    }
}
