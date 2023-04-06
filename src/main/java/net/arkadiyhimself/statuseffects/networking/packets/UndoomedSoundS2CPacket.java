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

public record UndoomedSoundS2CPacket() implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().getSoundManager().
                    play(SimpleSoundInstance.forUI(StatusEffectsSounds.UNDOOMED.getSound(), 1.0F, 1.0F));
        });
        context.setPacketHandled(true);
    }
    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, UndoomedSoundS2CPacket.class, UndoomedSoundS2CPacket::read);
    }
    public static UndoomedSoundS2CPacket read(FriendlyByteBuf packetBuf) {
        return new UndoomedSoundS2CPacket();
    }
    @Override
    public void write(FriendlyByteBuf packetBuf) {

    }
}
