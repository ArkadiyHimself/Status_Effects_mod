package net.arkadiyhimself.statuseffects.capability.DoomedEffect;

import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

public class DoomedEffect extends LivingEntityCapability {
    public DoomedEffect(LivingEntity entity) { super(entity); }
    public void updateData() { this.updateTracking(); }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.livingEntity.getId(), DoomedEffectAttacher.HAS_DOOMED_CAPABILITY_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() { return NetworkHandler.INSTANCE; }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isDoomed", this.isDoomed);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains("isDoomed")) {
            this.isDoomed = nbt.getBoolean("isDoomed");
        } else {
            this.isDoomed = false;
        }
    }
    private boolean isDoomed;
    public void setDoomed(boolean doomed) { this.isDoomed = doomed; }
    public boolean isDoomed() { return isDoomed; }
}
