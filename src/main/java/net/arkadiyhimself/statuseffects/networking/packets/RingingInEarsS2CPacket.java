package net.arkadiyhimself.statuseffects.networking.packets;

import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RingingInEarsS2CPacket {
    public RingingInEarsS2CPacket() {

    }
    public RingingInEarsS2CPacket(FriendlyByteBuf buf) {

    }
    public void toBytes(FriendlyByteBuf buf) {

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().getSoundManager().
                    play(SimpleSoundInstance.forUI(StatusEffectsSounds.RINGING_LONG.get(), 1.0F, 3.3F));
        });
        context.setPacketHandled(true);
        return true;
    }
}
