package net.arkadiyhimself.statuseffects.world.effects.effectsdostuff;

import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class doomed extends MobEffect {
    protected static final ResourceLocation SWARMING_SOULS = new ResourceLocation(StatusEffects.MODID, "textures/misc/swarming_souls.png");

    double x;
    double y;
    double z;
    boolean doplaysound = false;
    boolean dospawnsoul = false;
    boolean dowhisper = false;
    Random random = new Random();
    int whispercooldown = 80;

    int soulcooldown = 20;

    ArrayList<SoundEvent> whispers = new ArrayList<>(){{
        add(StatusEffectsSounds.WHISPER1.get());
        add(StatusEffectsSounds.WHISPER2.get());
        add(StatusEffectsSounds.WHISPER3.get());
        add(StatusEffectsSounds.WHISPER4.get());
        add(StatusEffectsSounds.WHISPER5.get());
    }};


    public doomed(MobEffectCategory mobEffectCategory, int p_19452_) {
        super(mobEffectCategory, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        doplaysound = pDuration == 20;
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        whispercooldown -= 1;
        soulcooldown -= 1;
        dowhisper = whispercooldown == 0;
        dospawnsoul = soulcooldown == 0;

        if (dospawnsoul) {
            soulcooldown = random.nextInt(6, 8);
            // using supplier to get entity's movement (no idea whether it even matters lmao)
            Supplier<Double> dx = () -> pLivingEntity.getDeltaMovement().x;
            Supplier<Double> dy = () -> pLivingEntity.getDeltaMovement().y;
            Supplier<Double> dz = () -> pLivingEntity.getDeltaMovement().z;
            // here im using circular function for X and Z (X**2 + Z**2 = R**2) coordinates to make a horizontal circle
            // Y variants are just a vertical line
            y = random.nextDouble(0.3, 1.5);
            x = random.nextDouble(-0.5, 0.5);
            z = Math.sqrt(0.25 - x * x);
            // here game randomly decides to make Z coordinate negative
            boolean negativeZ = random.nextBoolean();
            z = negativeZ ? z * (-1) : z;
            // in the end, the area in which soul particles can spawn looks like side of a cylinder
            Minecraft.getInstance().level.addParticle(StatusEffectsParticles.DOOMED_SOUL.get(), true,
                    pLivingEntity.getX() + x, pLivingEntity.getY() + y, pLivingEntity.getZ() + z,
                    dx.get() * 2.6, dy.get() * 0.2 + 0.1, dz.get() * 2.6);
        }

        if (dowhisper && !doplaysound) {
            // the game randomly decides which whisper sound to play
            int num = random.nextInt(0,  5);
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(whispers.get(num), 1F, 1F));
            whispercooldown = random.nextInt(200, 240);
        }

        if (doplaysound && pLivingEntity == Minecraft.getInstance().player) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(StatusEffectsSounds.UNDOOMED.get(), 1F, 1F));
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

}
