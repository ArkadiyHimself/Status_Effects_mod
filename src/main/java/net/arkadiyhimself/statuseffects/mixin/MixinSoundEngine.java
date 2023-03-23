package net.arkadiyhimself.statuseffects.mixin;

import net.arkadiyhimself.statuseffects.effects.SE_MobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class MixinSoundEngine {
	@Redirect(method = "Lnet/minecraft/client/sounds/SoundEngine;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getVolume()F"))
	private float statuseffects_calculateSoundVolume(SoundInstance sound) {
		if(Minecraft.getInstance().level != null && Minecraft.getInstance().player.hasEffect(SE_MobEffect.DEAFENING.get())) {
		return 0.1F; } else { return 1F; }
	}

	@Inject(method = "calculateVolume*", at = @At("RETURN"), cancellable = true)
	private void statuseffects_calculateTickableSoundVolume(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(SE_MobEffect.DEAFENING.get())) {
			cir.setReturnValue(0.1F);
		}
	}
}
