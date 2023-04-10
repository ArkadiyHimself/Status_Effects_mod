package net.arkadiyhimself.statuseffects.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.DoomedEffect.DoomedEffect;
import net.arkadiyhimself.statuseffects.capability.DoomedEffect.DoomedEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffect;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffectAttacher;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.arkadiyhimself.statuseffects.networking.packets.DoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = StatusEffects.MODID)
public class DoomedEffectStuff implements IGuiOverlay {
    static LocalPlayer player = Minecraft.getInstance().player;
    public static Supplier<Double> maxHealth = () -> (double) Math.ceil(Minecraft.getInstance().player.getMaxHealth());
    public static Supplier<Double> currentHealth = () -> (double) Math.ceil(Minecraft.getInstance().player.getHealth());
    public static final ResourceLocation DOOMED_HEARTS_EMPTY =
            new ResourceLocation(StatusEffects.MODID, "textures/gui/doomed_hearts/doomed_heart_empty.png");
    public static final ResourceLocation DOOMED_HEARTS_FULL =
            new ResourceLocation(StatusEffects.MODID, "textures/gui/doomed_hearts/doomed_heart_full.png");
    public static final ResourceLocation DOOMED_HEARTS_FULL_HALF =
            new ResourceLocation(StatusEffects.MODID, "textures/gui/doomed_hearts/doomed_heart_full_half.png");
    public static final ResourceLocation DOOMED_HEARTS_EMPTY_HALF =
            new ResourceLocation(StatusEffects.MODID, "textures/gui/doomed_hearts/doomed_heart_empty_half.png");

    static boolean isDoomed() {
        AtomicBoolean isdoomed = new AtomicBoolean(false);
        DoomedEffectAttacher.getHasDoomed(player).ifPresent(doomedEffect -> {
            isdoomed.set(doomedEffect.isDoomed());
        });
        return isdoomed.get();
    }
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(DoomedEffect.class);
    }
    public static final IGuiOverlay DOOMED_HEARTS = (((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        if (!Minecraft.getInstance().gameMode.canHurtPlayer()) { return; }
        if (!Minecraft.getInstance().player.hasEffect(StatusEffectsMobEffect.DOOMED.get())) { return; }
        int x = screenWidth / 2;
        int y = screenHeight;
        int maxHealthRows = (int) (maxHealth.get() / 20);
        int currentHealthRows = (int) (currentHealth.get() / 20);
        boolean hasHalfMax = maxHealth.get() % 2 > 0;
        boolean hasHalfCurrent = currentHealth.get() % 2 > 0;
        double remainingMaxHealth = maxHealth.get() - maxHealthRows * 20;
        double remainingCurrentHealth = currentHealth.get() - currentHealthRows * 20;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);

        RenderSystem.setShaderTexture(0, DOOMED_HEARTS_EMPTY);
        for (int row = 0; row < maxHealthRows; row++) {
            for (int i = 0; i < 10; i++) {
                GuiComponent.blit(poseStack, x - 91 + (i * 8), y - 47 - (row - 1) * 8, 0, 0,
                        9,9,9,9);
            }
        }
        for (int i = 0; i < (int) remainingMaxHealth / 2; i++) {
            GuiComponent.blit(poseStack, x - 91 + (i * 8), y - 47 - (maxHealthRows - 1) * 8, 0, 0,
                    9,9,9,9);
        }
        RenderSystem.setShaderTexture(0, DOOMED_HEARTS_EMPTY_HALF);
        if (hasHalfMax) {
            GuiComponent.blit(poseStack, x - 91 + ((int) remainingMaxHealth / 2) * 8, y - 47 - (maxHealthRows - 1) * 8, 0, 0,
                    9, 9, 8, 8);
        }

        RenderSystem.setShaderTexture(0, DOOMED_HEARTS_FULL);
        for (int row = 0; row < currentHealthRows; row++) {
            for (int i = 0; i < 10; i++) {
                GuiComponent.blit(poseStack, x - 91 + (i * 8), y - 47 - (row - 1) * 8, 0, 0,
                        9,9,9,9);
            }
        }
        for (int i = 0; i < (int) remainingCurrentHealth / 2; i++) {
            GuiComponent.blit(poseStack, x - 91 + (i * 8), y - 47 - (currentHealthRows - 1) * 8, 0, 0,
                    9,9,9,9);
        }
        RenderSystem.setShaderTexture(0, DOOMED_HEARTS_FULL_HALF);
        if (hasHalfCurrent) {
            GuiComponent.blit(poseStack, x - 91 + ((int) remainingCurrentHealth / 2) * 8, y - 47 - (currentHealthRows - 1) * 8, 0, 0,
                    9, 9, 8, 8);
        }
    }));
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

    @SubscribeEvent
    public static void doomedApplied(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DOOMED.get()) {
            LivingEntity entity = event.getEntity();
            if (entity instanceof Player) {
                NetworkHandler.sentToPlayer(new DoomedSoundS2CPacket(), (ServerPlayer) entity);
            }
            DoomedEffectAttacher.getHasDoomed(entity).ifPresent(doomedEffect -> {
                doomedEffect.setDoomed(true);
                doomedEffect.updateData();
            });
        }
    }
    @SubscribeEvent
    public static void doomedEnded(MobEffectEvent.Remove event) {
        if (event.getEffectInstance().getEffect() == StatusEffectsMobEffect.DOOMED.get()) {
            DoomedEffectAttacher.getHasDoomed(event.getEntity()).ifPresent(doomedEffect -> {
                doomedEffect.setDoomed(false);
                doomedEffect.updateData();
            });
        }
    }


    // don't pay attention to this one, I have specific plans on it
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void cancelVanillaHearts(RenderGuiOverlayEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.getOverlay().equals(VanillaGuiOverlay.PLAYER_HEALTH.type()) && isDoomed(player)) {
            event.setCanceled(true);
            Gui gui = Minecraft.getInstance().gui;

        }
    }
    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {}
}
