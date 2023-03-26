package net.arkadiyhimself.statuseffects.networking.packets;

import net.arkadiyhimself.statuseffects.sound.SE_Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
                    play(SimpleSoundInstance.forUI(SE_Sounds.RINGING_LONG.get(), 1.0F, 3.3F));
        });
        context.setPacketHandled(true);
        return true;
    }
}
