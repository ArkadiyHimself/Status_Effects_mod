package net.arkadiyhimself.statuseffects.mixin;

import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(EntityTypeTagsProvider.class)
public abstract class MixinEntityTypeTagsProvider extends IntrinsicHolderTagsProvider {

    public MixinEntityTypeTagsProvider(PackOutput p_256164_, ResourceKey p_256155_, CompletableFuture p_256488_, Function p_256168_) {
        super(p_256164_, p_256155_, p_256488_, p_256168_);
    }

}
