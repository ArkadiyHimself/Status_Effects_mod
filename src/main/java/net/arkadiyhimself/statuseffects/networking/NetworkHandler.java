package net.arkadiyhimself.statuseffects.networking;

import com.google.common.collect.ImmutableList;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.DoomedEffect.DoomedEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.FreezeEffect.FreezeEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffectAttacher;
import net.arkadiyhimself.statuseffects.networking.packets.*;
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
                .add(AttackDeniedSoundS2CPacket::register)
                .add(KickOutOfGuiS2CPacket::register)
                .build();
        SimpleEntityCapabilityStatusPacket.registerRetriever(StunEffectAttacher.STUN_EFFECT_CAPABILITY_RL, StunEffectAttacher::getStunEffectUnwrap);
        SimpleEntityCapabilityStatusPacket.registerRetriever(FreezeEffectAttacher.HAS_FREEZE_CAPABILITY_RL, FreezeEffectAttacher::getHasFreezeUnwrap);
        SimpleEntityCapabilityStatusPacket.registerRetriever(DisarmEffectAttacher.HAS_DISARM_CAPABILITY_RL, DisarmEffectAttacher::getHasDisarmUnwrap);
        SimpleEntityCapabilityStatusPacket.registerRetriever(DoomedEffectAttacher.HAS_DOOMED_CAPABILITY_RL, DoomedEffectAttacher::getHasDoomedUnwrap);
        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sentToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}
