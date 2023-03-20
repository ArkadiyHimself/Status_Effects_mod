package net.arkadiyhimself.statuseffects.mixin;


import com.google.common.collect.Maps;
import net.arkadiyhimself.statuseffects.effects.ModMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Warden.class)
public abstract class MixinWarden extends LivingEntity {
    public MixinWarden(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/world/entity/monster/warden/Warden;shouldListen(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/gameevent/GameEventListener;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z", cancellable = true)
    protected void deafWarden(ServerLevel p_219370_, GameEventListener p_219371_, BlockPos p_219372_, GameEvent p_219373_, GameEvent.Context p_219374_, CallbackInfoReturnable<Boolean> cir) {
        if(this.hasEffect(ModMobEffect.DEAFENING.get())) {
            cir.setReturnValue(false);
        }
    }
}
