package net.arkadiyhimself.statuseffects.capability;

import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

public class FreezeEffect extends LivingEntityCapability {
    public void updateData() {
        this.updateTracking();
    }
    public FreezeEffect(LivingEntity entity) {
        super(entity);
    }
    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.livingEntity.getId(), FreezeEffectAttacher.HAS_FREEZE_CAPABILITY_RL, this);
    }
    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }
    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("hasFreezeEffect", hasFreezeEffect);
        tag.putInt("currentDuration", currentDuration);
        tag.putInt("maxDuration", maxDuration);
        tag.putInt("delayFreezeDecay", delayFreezeDecay);
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains("hasFreezeEffect")) {
            this.hasFreezeEffect = nbt.getBoolean("hasFreezeEffect");
        } else {
            this.hasFreezeEffect = false;
        }
        if (nbt.contains("currentDuration")) {
            this.currentDuration = nbt.getInt("currentDuration");
        } else {
            this.currentDuration = 0;
        }
        if (nbt.contains("maxDuration")) {
            this.maxDuration = nbt.getInt("maxDuration");
        } else {
            this.maxDuration = 1;
        }
        if (nbt.contains("delayFreezeDecay")) {
            this.delayFreezeDecay = nbt.getInt("delayFreezeDecay");
        } else {
            this.delayFreezeDecay = 0;
        }
    }
    private boolean hasFreezeEffect;
    private int currentDuration;
    private int maxDuration;
    private int delayFreezeDecay;

    // has freeze effect
    public boolean isFrozen() {
        return hasFreezeEffect;
    }
    public void setFrozen(boolean isfrozen) {
        this.hasFreezeEffect = isfrozen;
    }

    // current duration
    public void setCurrentDuration(int amount) {
        this.currentDuration = amount;
    }
    public void addCurrentDuration(int amount) {
        this.currentDuration += amount;
    }
    public void subCurrentDuration(int amount) {
        this.currentDuration = Math.max(currentDuration - amount, 0);
    }
    public int getCurrentDuration() {
        return currentDuration;
    }

    // max duration
    public void setMaxDuration(int amount) {
        this.maxDuration = amount;
    }
    public int getMaxDuration() {
        return maxDuration;
    }

    // delay freeze decay
    public void setDelayFreezeDecay(int amount) {
        this.delayFreezeDecay = amount;
    }
    public void addDelayFreezeDecay(int amount) {
        this.delayFreezeDecay += amount;
    }
    public void subDelayFreezeDecay(int amount) {
        this.delayFreezeDecay = Math.max(0, this.delayFreezeDecay - amount);
    }
    public int getDelayFreezeDecay() {
        return delayFreezeDecay;
    }

}
