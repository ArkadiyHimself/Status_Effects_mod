package net.arkadiyhimself.statuseffects.capability;

import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

public class StunScale extends LivingEntityCapability {

    // update data
    public void updateData() {
        this.updateTracking();
    }

    // SyncedCapability related stuff
    public StunScale(LivingEntity entity) {
        super(entity);
    }
    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.livingEntity.getId(), StunScaleAttacher.STUN_POINTS_CAPABILITY_RL, this);
    }
    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }
    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("stunPoints", this.stunPoints);
        tag.putInt("initDuration", this.stunDurationInitial);
        tag.putInt("currentDuration", this.currentDuration);
        tag.putBoolean("isStunned", this.isStunned);
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains("stunPoints", Tag.TAG_INT)) {
            this.stunPoints = nbt.getInt("stunPoints");
        } else {
            this.stunPoints = this.minStunPoints;
        }

        if (nbt.contains("initDuration", Tag.TAG_INT)) {
            this.stunDurationInitial = nbt.getInt("initDuration");
        } else {
            this.stunDurationInitial = 1;
        }

        if (nbt.contains("currentDuration", Tag.TAG_INT)) {
            this.currentDuration = nbt.getInt("currentDuration");
        } else {
            this.currentDuration = 0;
        }

        if (nbt.contains("isStunned")) {
            this.isStunned = nbt.getBoolean("isStunned");
        } else {
            this.isStunned = false;
        }
    }

    // my variables
    private int stunPoints; // the current amount of stun points
    private final int minStunPoints = 0; // the minimum amount of stun points
    private int maxStunPoints = 200; // the amount of stun points entity can get before getting stunned
    private int decayDelay; // the delay before stun points start decaying after each hit
    private final int defaultPointsFromHit = 50; // the default amount of points gotten from being hit
    private final int defaultDecayDelay = 50; // the default delay before stun points start decaying after each hit
    private final int defaultStunDurationFromHits = 50; // stun duration from being stunned by being hit
    private int stunDurationInitial; // stun duration from being stunned in any way
    private int currentDuration; // current duration
    boolean isStunned;

    // getters, setters, etc.

    // stun points
    public int getStunPoints() {
        return stunPoints;
    }
    public void setStunPoints(int amount) {
        if (amount >= this.maxStunPoints) {
            this.stunPoints = maxStunPoints;
        } else {
            this.stunPoints = Math.max(minStunPoints, amount);
        }
    } // in this setter I made sure that entity's current stun points won't go above maximum nor below minimum
    public void addStunPoints(int amount) {
        this.stunPoints = Math.min(maxStunPoints, this.stunPoints + amount);
    }
    public void subStunPoints(int amount) {
        this.stunPoints = Math.max(minStunPoints, this.stunPoints - amount);
    }

    // minimum stun points, don't see a reason adding anything else aside of getter
    public int getMinStunPoints() {
        return minStunPoints;
    }

    // maximum stun points
    public int getMaxStunPoints() {
        return maxStunPoints;
    }
    public void setMaxStunPoints(int amount) {
        this.maxStunPoints = amount;
    }

    // delay before decaying
    public int getDecayDelay() {
        return decayDelay;
    }
    public void setDecayDelay(int amount) {
        this.decayDelay = amount;
    }
    public void addDecayDelay(int amount) {
        this.decayDelay += amount;
    }
    public void subDecayDelay(int amount) {
        this.decayDelay = Math.max(0, this.decayDelay - amount);
    }

    // default values, shouldn't be changed anyway
    public int getDefaultDecayDelay() {
        return defaultDecayDelay;
    }
    public int getDefaultPointsFromHit() {
        return defaultPointsFromHit;
    }

    // default stun duration form
    public int getDefaultStunDurationFromHits() {
        return defaultStunDurationFromHits;
    }

    // stun duration
    public int getStunDurationInitial() {
        return stunDurationInitial;
    }
    public void setStunDurationInitial(int duration) {
        this.stunDurationInitial = duration;
    }

    // current stun duration
    public int getCurrentDuration() {
        return currentDuration;
    }
    public void setCurrentDuration(int duration) {
        this.currentDuration = duration;
    }

    // is entity stunned
    public void setStunned(boolean stunned) {
        isStunned = stunned;
    }
    public boolean isStunned() {
        return this.isStunned;
    }
}
