package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.StunScale;
import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class StunScaleStuff {
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
    public static final ResourceLocation STUN_BAR_GUI = new ResourceLocation(StatusEffects.MODID, "textures/gui/stun_bar_gui.png");
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(StunScale.class);
    }
    @SubscribeEvent
    public static void deathReset(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            StunScaleAttacher.getStunScale(player).ifPresent(stunScale -> {
                stunScale.setStunPoints(0);
                stunScale.updateData();
            });
        }
    }
    @SubscribeEvent
    public static void modeChangeReset(PlayerEvent.PlayerChangeGameModeEvent event) {
        StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
            if(event.getNewGameMode() == GameType.CREATIVE || event.getNewGameMode() == GameType.SPECTATOR) {
                stunScale.setStunPoints(0);
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
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                stunScale.setStunPoints(0);
                stunScale.setDecayDelay(0);
                stunScale.setStunned(true);
                stunScale.updateData();
            });
        }
    }
    @SubscribeEvent
    public static void stunEnded(MobEffectEvent.Remove event) {
        if(event.getEffectInstance().getEffect() == StatusEffectsMobEffect.STUN.get()) {
            StunScaleAttacher.getStunScale(event.getEntity()).ifPresent(stunScale -> {
                stunScale.setStunned(false);
                stunScale.updateData();
            });
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
                if (event.getAmount() > 0) {
                    stunScale.addStunPoints(50);
                    stunScale.setDecayDelay(50);
                }
                if (stunScale.getStunPoints() == stunScale.getMaxStunPoints()) {
                    int duration = stunScale.getDefaultStunDurationFromHits();
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsMobEffect.STUN.get(), duration, 1, false, false, false));
                }
                stunScale.updateData();
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public static void renderStunBarExp(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(event.getOverlay().equals(VanillaGuiOverlay.EXPERIENCE_BAR.type())) {
            StunScaleAttacher.getStunScale(minecraft.player).ifPresent(stunScale -> {
                if (stunScale.getStunPoints() > 0 && !stunScale.isStunned()) {
                    int currentPoints = stunScale.getStunPoints();
                    int maxStunPoint = stunScale.getMaxStunPoints();
                    int stunPercent = (int) Math.ceil((float) currentPoints / (float) maxStunPoint * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, STUN_BAR_GUI);

                    int y = event.getWindow().getGuiScaledHeight() - 29;

                    GuiComponent.blit(poseStack, x, y, 0, 0, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 5F, stunPercent, 5, 182, 182);

                    String titleStunBar = I18n.get("gui.statuseffects.stun_bar");
                    int i1 = (event.getWindow().getGuiScaledWidth() - minecraft.font.width(titleStunBar)) / 2;
                    int j1 = event.getWindow().getGuiScaledHeight() - 35;

                    int color = 5832704;

/*                    minecraft.font.draw(poseStack, titleStunBar, 1, 0, color);
                    minecraft.font.draw(poseStack, titleStunBar, -1, 0, color);
                    minecraft.font.draw(poseStack, titleStunBar, 0, 1, color);
                    minecraft.font.draw(poseStack, titleStunBar, 0, -1, color);
                    minecraft.font.draw(poseStack, titleStunBar, 0, 0, 16770638);
*/                } else if (minecraft.player.hasEffect(StatusEffectsMobEffect.STUN.get())) {
                    int currentStun = stunScale.getCurrentDuration();
                    int initStun = stunScale.getStunDurationInitial();
                    int durationPercent = (int) Math.ceil((float) currentStun / (float) initStun * 182);
                    PoseStack poseStack = event.getPoseStack();
                    int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                    event.setCanceled(true);
                    RenderSystem.setShaderTexture(0, STUN_BAR_GUI);
                    int y = event.getWindow().getGuiScaledHeight() - 29;
                    GuiComponent.blit(poseStack, x, y, 0, 10F, 182, 5, 182, 182);
                    GuiComponent.blit(poseStack, x, y, 0, 0, 15F, durationPercent, 5, 182, 182);
                }
            });
        }
    }
}
