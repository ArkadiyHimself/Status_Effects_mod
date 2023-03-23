package net.arkadiyhimself.statuseffects.effects.effectsdostuff;

import net.arkadiyhimself.statuseffects.sound.SE_Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class doomed extends MobEffect {

    boolean doplaysound = false;

    public doomed(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        doplaysound = p_19455_ == 20;
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(doplaysound){ Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SE_Sounds.UNDOOMED.get(), 1.0F, 5.0F)); }
        super.applyEffectTick(p_19467_, p_19468_);
    }

}
