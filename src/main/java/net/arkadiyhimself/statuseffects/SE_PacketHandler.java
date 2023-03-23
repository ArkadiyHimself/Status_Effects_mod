package net.arkadiyhimself.statuseffects;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class SE_PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel RINGING = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Status_Effects.MODID, "ringing_long"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    class MessagePacket {
        public void encoder(FriendlyByteBuf buffer) {
            // Write to buffer
        }

        public static MessagePacket decoder(FriendlyByteBuf buffer) {
            // Create packet from buffer data
            return null;
        }

        public void messageConsumer(Supplier<NetworkEvent.Context> ctx) {
            // Handle message
        }
    }


}
