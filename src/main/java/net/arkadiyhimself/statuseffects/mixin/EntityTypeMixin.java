package net.arkadiyhimself.statuseffects.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Entity.class)
public class EntityTypeMixin {
    public final boolean freezeImmune;

    public EntityTypeMixin(boolean freezeImmune) {
        this.freezeImmune = freezeImmune;
    }

//    @Inject(at = @At(value = "TAIL"), method = "Lnet/minecraft/world/entity/EntityType;<init>(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;ZZZZLcom/google/common/collect/ImmutableSet;Lnet/minecraft/world/entity/EntityDimensions;IILnet/minecraft/world/flag/FeatureFlagSet;Ljava/util/function/Predicate;Ljava/util/function/ToIntFunction;Ljava/util/function/ToIntFunction;Ljava/util/function/BiFunction;)V")
}
