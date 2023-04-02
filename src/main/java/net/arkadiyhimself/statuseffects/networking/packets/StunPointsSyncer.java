package net.arkadiyhimself.statuseffects.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StunPointsSyncer {
    public StunPointsSyncer() {}
    public StunPointsSyncer(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // just created it
        });
        return true;
    }
}
