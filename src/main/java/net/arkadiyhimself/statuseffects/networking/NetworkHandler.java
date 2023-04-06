package net.arkadiyhimself.statuseffects.networking;

import com.google.common.collect.ImmutableList;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.FreezeEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.arkadiyhimself.statuseffects.networking.packets.DoomedSoundS2CPacket;
import net.arkadiyhimself.statuseffects.networking.packets.RingingInEarsS2CPacket;
import net.arkadiyhimself.statuseffects.networking.packets.UndoomedSoundS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(StatusEffects.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int packetID = 0;
    private static int getNextId() {
        return packetID++;
    }
    public static void register() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add(SimpleEntityCapabilityStatusPacket::register)
                .add(DoomedSoundS2CPacket::register)
                .add(UndoomedSoundS2CPacket::register)
                .add(RingingInEarsS2CPacket::register)
                .build();
        SimpleEntityCapabilityStatusPacket.registerRetriever(StunScaleAttacher.STUN_POINTS_CAPABILITY_RL,StunScaleAttacher::getStunScaleUnwrap);
        SimpleEntityCapabilityStatusPacket.registerRetriever(FreezeEffectAttacher.HAS_FREEZE_CAPABILITY_RL, FreezeEffectAttacher::getHasFreezeUnwrap);
        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sentToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

}
