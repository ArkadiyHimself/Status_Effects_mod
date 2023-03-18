package net.arkadiyhimself.statuseffects.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;

public class DeafEntities extends Warden {

    public DeafEntities(EntityType<? extends Monster> p_219350_, Level p_219351_) {
        super(p_219350_, p_219351_);
    }

    @Override
    public boolean shouldListen(ServerLevel p_219370_, GameEventListener p_219371_, BlockPos p_219372_, GameEvent p_219373_, GameEvent.Context p_219374_) {
        if (this.hasEffect(ModMobEffect.DEAFENING.get())) { return false; }
        else { return super.shouldListen(p_219370_, p_219371_, p_219372_, p_219373_, p_219374_); }
    }
}
