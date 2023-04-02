package net.arkadiyhimself.statuseffects.capability;

import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

public class StunScale extends LivingEntityCapability {

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
    public void setStunPoints(int amount, boolean sync) {
        if (amount >= this.maxStunPoints) {
            this.stunPoints = maxStunPoints;
        } else {
            this.stunPoints = Math.max(minStunPoints, amount);
        }
        if (sync) {
            this.updateTracking();
        }
    } // in this setter I made sure that entity's current stun points won't go above maximum nor below minimum
    public void addStunPoints(int amount, boolean sync) {
        this.stunPoints = Math.min(maxStunPoints, this.stunPoints + amount);
        if (sync) {
            this.updateTracking();
        }
    }
    public void subStunPoints(int amount, boolean sync) {
        this.stunPoints = Math.max(minStunPoints, this.stunPoints - amount);
        if (sync) {
            this.updateTracking();
        }
    }

    // minimum stun points, don't see a reason adding anything else aside of getter
    public int getMinStunPoints() {
        return minStunPoints;
    }

    // maximum stun points
    public int getMaxStunPoints() {
        return maxStunPoints;
    }
    public void setMaxStunPoints(int amount, boolean sync) {
        this.maxStunPoints = amount;
        if (sync) {
            this.updateTracking();
        }
    }

    // delay before decaying
    public int getDecayDelay() {
        return decayDelay;
    }
    public void setDecayDelay(int amount, boolean sync) {
        this.decayDelay = amount;
        if (sync) {
            this.updateTracking();
        }
    }
    public void addDecayDelay(int amount, boolean sync) {
        this.decayDelay += amount;
        if (sync) {
            this.updateTracking();
        }
    }
    public void subDecayDelay(int amount, boolean sync) {
        this.decayDelay = Math.max(0, this.decayDelay - amount);
        if (sync) {
            this.updateTracking();
        }
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
    public void setStunDurationInitial(int duration, boolean sync) {
        this.stunDurationInitial = duration;
        if (sync) {
            this.updateTracking();
        }
    }

    // current stun duration
    public int getCurrentDuration() {
        return currentDuration;
    }
    public void setCurrentDuration(int duration, boolean sync) {
        this.currentDuration = duration;
        if (sync) {
            this.updateTracking();
        }
    }

    // is entity stunned
    public void setStunned(boolean stunned, boolean sync) {
        isStunned = stunned;
        if (sync) {
            this.updateTracking();
        }
    }
    public boolean getStunned() {
        return this.isStunned;
    }
}
