package net.arkadiyhimself.statuseffects.networking.packets;

import dev._100media.capabilitysyncer.network.IPacket;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public record RingingInEarsS2CPacket() implements IPacket {

    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().getSoundManager().
                    play(SimpleSoundInstance.forUI(StatusEffectsSounds.RINGING_LONG.get(), 1.0F, 3.3F));
        });
        context.setPacketHandled(true);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {

    }
    public static RingingInEarsS2CPacket read(FriendlyByteBuf packetBuf) {
        return new RingingInEarsS2CPacket();
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, RingingInEarsS2CPacket.class, RingingInEarsS2CPacket::read);
    }
}
