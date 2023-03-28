package net.arkadiyhimself.statuseffects.effects.effectsdostuff;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.arkadiyhimself.statuseffects.networking.Messages;
import net.arkadiyhimself.statuseffects.networking.packets.UndoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.particles.SE_Particles;
import net.arkadiyhimself.statuseffects.sound.SE_Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class doomed extends MobEffect {
    protected static final ResourceLocation SWARMING_SOULS = new ResourceLocation(Status_Effects.MODID, "textures/misc/swarming_souls.png");

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
        add(SE_Sounds.WHISPER1.get());
        add(SE_Sounds.WHISPER2.get());
        add(SE_Sounds.WHISPER3.get());
        add(SE_Sounds.WHISPER4.get());
        add(SE_Sounds.WHISPER5.get());
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
            Minecraft.getInstance().level.addParticle(SE_Particles.DOOMED_SOUL.get(), true,
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
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.UNDOOMED.get(), 1F, 1F));
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

}
