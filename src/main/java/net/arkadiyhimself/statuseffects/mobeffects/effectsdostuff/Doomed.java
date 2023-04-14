package net.arkadiyhimself.statuseffects.mobeffects.effectsdostuff;

import net.arkadiyhimself.statuseffects.capability.DoomedEffect.DoomedEffectAttacher;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.arkadiyhimself.statuseffects.networking.packets.UndoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.sound.SoundWhispers;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class Doomed extends MobEffect {
    boolean isNotDoomed;
    double x;
    double y;
    double z;
    boolean doplaysound = false;
    boolean dospawnsoul = false;
    boolean dowhisper = false;
    Random random = new Random();
    int whispercooldown = 80;

    int soulcooldown = 20;
    public static ArrayList<RegistryObject<SimpleParticleType>> doomedSouls = new ArrayList<>() {{
        add(StatusEffectsParticles.DOOMED_SOUL1);
        add(StatusEffectsParticles.DOOMED_SOUL2);
        add(StatusEffectsParticles.DOOMED_SOUL3);
    }};

    public Doomed(MobEffectCategory mobEffectCategory, int p_19452_) {
        super(mobEffectCategory, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        doplaysound = pDuration == 20;
        isNotDoomed = pDuration < 2;
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        // cool down decays
        whispercooldown -= 1;
        soulcooldown -= 1;

        // when cool down is 0, sound plays and particles appear
        dowhisper = whispercooldown == 0;
        dospawnsoul = soulcooldown == 0;

        if (dospawnsoul) {
            // randomly choose one of doomed souls particle
            int num = random.nextInt(0, 3);

            // getting entity's height and width
            float radius = pLivingEntity.getBbWidth() * (float) 0.7;
            float height = pLivingEntity.getBbHeight();
            soulcooldown = random.nextInt(6, 8);
            // using supplier to get entity's movement (no idea whether it even matters lmao)
            Supplier<Double> dx = () -> pLivingEntity.getDeltaMovement().x;
            Supplier<Double> dy = () -> pLivingEntity.getDeltaMovement().y;
            Supplier<Double> dz = () -> pLivingEntity.getDeltaMovement().z;
            // here im using circular function for X and Z (X**2 + Z**2 = R**2) coordinates to make a horizontal circle
            // Y variants are just a vertical line
            y = random.nextDouble(0, height * 0.8);
            x = random.nextDouble(-radius, radius);
            z = Math.sqrt(radius * radius - x * x);
            // here game randomly decides to make Z coordinate negative
            boolean negativeZ = random.nextBoolean();
            z = negativeZ ? z * (-1) : z;
            // in the end, the area in which soul particles can spawn looks like side of a cylinder
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().level.addParticle(doomedSouls.get(num).get(), true,
                        pLivingEntity.getX() + x, pLivingEntity.getY() + y, pLivingEntity.getZ() + z,
                        dx.get() * 1.5, dy.get() * 0.2 + 0.1, dz.get() * 1.5);
            }
        }

        if (dowhisper && !doplaysound && pLivingEntity == Minecraft.getInstance().player) {
            // the game randomly decides which whisper sound to play
            int num = random.nextInt(0,  SoundWhispers.amount);
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundWhispers.whispers.get(num).getSound(), 1F, 1F));
            // randomly decide cool down between 10 and 12 seconds
            whispercooldown = random.nextInt(200, 240);
        }

        if (doplaysound && pLivingEntity instanceof ServerPlayer player) {
//            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(StatusEffectsSounds.UNDOOMED.get(), 1F, 1F));
            NetworkHandler.sentToPlayer(new UndoomedSoundS2CPacket(), player);
        }
        DoomedEffectAttacher.getHasDoomed(pLivingEntity).ifPresent(doomedEffect -> {
            doomedEffect.setDoomed(!isNotDoomed);
        });
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

}
