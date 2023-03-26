package net.arkadiyhimself.statuseffects.networking;

import net.arkadiyhimself.statuseffects.Status_Effects;
import net.arkadiyhimself.statuseffects.networking.packets.DoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.networking.packets.RingingInEarsS2CPacket;
import net.arkadiyhimself.statuseffects.networking.packets.UndoomedSoundS2CPacket;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {
    private static SimpleChannel packetInstance;
    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }
    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Status_Effects.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        packetInstance = net;

        net.messageBuilder(RingingInEarsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RingingInEarsS2CPacket::new)
                .encoder(RingingInEarsS2CPacket::toBytes)
                .consumerMainThread(RingingInEarsS2CPacket::handle)
                .add();
        net.messageBuilder(DoomedSoundS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DoomedSoundS2CPacket::new)
                .encoder(DoomedSoundS2CPacket::toBytes)
                .consumerMainThread(DoomedSoundS2CPacket::handle)
                .add();
        net.messageBuilder(UndoomedSoundS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UndoomedSoundS2CPacket::new)
                .encoder(UndoomedSoundS2CPacket::toBytes)
                .consumerMainThread(UndoomedSoundS2CPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG msg) {
        packetInstance.sendToServer(msg);
    }

    public static <MSG> void sentToPlayer(MSG msg, ServerPlayer player) {
        packetInstance.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

}
