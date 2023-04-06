package net.arkadiyhimself.statuseffects.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.arkadiyhimself.statuseffects.client.AboveEntititesRenderer.RenderAboveEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher {
    @Shadow public abstract Quaternionf cameraOrientation();

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    shift = At.Shift.AFTER
            )
    )
    private void StatusEffectsRenderStunBar(Entity pEntity, double pX, double pY, double pZ, float pRotationYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        RenderAboveEntity.renderStunBar(pEntity, pMatrixStack, pBuffer, cameraOrientation());
//        RenderAboveEntity.renderSnowCrystal(pEntity, pMatrixStack, pBuffer, cameraOrientation());
    }
}
