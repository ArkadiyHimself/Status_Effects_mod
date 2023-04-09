package net.arkadiyhimself.statuseffects.capability.DisarmEffect;

import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

public class DisarmEffect extends LivingEntityCapability {
    public DisarmEffect(LivingEntity entity) { super(entity); }
    public void updateData() { this.updateTracking(); }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.livingEntity.getId(), DisarmEffectAttacher.HAS_DISARM_CAPABILITY_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() { return NetworkHandler.INSTANCE; }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isDisarmed", this.isDisarmed);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains("isDisarmed")) {
            this.isDisarmed = nbt.getBoolean("isDisarmed");
        } else {
            this.isDisarmed = false;
        }
    }
    private boolean isDisarmed;
    public void setDisarmed(boolean disarmed) { this.isDisarmed = disarmed; }
    public boolean isDisarmed() { return isDisarmed; }
}
