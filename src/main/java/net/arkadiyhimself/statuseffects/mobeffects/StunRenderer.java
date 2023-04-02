package net.arkadiyhimself.statuseffects.mobeffects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.arkadiyhimself.statuseffects.client.StunRender.StunBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

public class StunRenderer {
    public static void renderStunBar(Entity entity, PoseStack poseStack,
                                     MultiBufferSource buffers, Quaternionf cameraOrientation) {
        final Minecraft mc = Minecraft.getInstance();
        if (!(entity instanceof LivingEntity living) || !entity.getPassengers().isEmpty()) {
            return;
        }
        final boolean boss = !entity.canChangeDimensions();
        poseStack.pushPose();
        poseStack.popPose();
        final float globalScale = 0.0267F;
        poseStack.translate(0, entity.getBbHeight() + 0.5, 0);
        poseStack.mulPose(cameraOrientation);
        final int light = 0xF000F0;
        int r = 255;
        int g = 255;
        int b = 255;
        poseStack.pushPose();
        poseStack.popPose();
        poseStack.scale(-globalScale, -globalScale, -globalScale);

        VertexConsumer stunBar = buffers.getBuffer(StunBar.BAR_TEXTURE_TYPE);
        StunScaleAttacher.getStunScale(living).ifPresent(stunScale -> {
//        living.getCapability(StunScaleAttacher.STUN_POINTS_CAPABILITY).ifPresent(stunScale -> {
            if(stunScale.getStunPoints() > 0) {
                stunBar.vertex(poseStack.last().pose(), -32, 0, 0.001F).color(r, g, b, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
                stunBar.vertex(poseStack.last().pose(), -32, 8, 0.001F).color(r, g, b, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                stunBar.vertex(poseStack.last().pose(), 32, 8, 0.001F).color(r, g, b, 255).uv(1.0F, 0.5F).uv2(light).endVertex();
                stunBar.vertex(poseStack.last().pose(), 32, 0, 0.001F).color(r, g, b, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
            }
        });

        // filling
/*        double pointsBeforeStunned = ((StunScaleInterface) living).getPointsBeforeStunned();
        double currentStunPoints = ((StunScaleInterface) living).getCurrentStunDuration();
        float currentStunPercent = (float) (currentStunPoints / pointsBeforeStunned);
        stunBar.vertex(poseStack.last().pose(), -32, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
        stunBar.vertex(poseStack.last().pose(), -32, 8, 0.002F).color(r, g, b, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        stunBar.vertex(poseStack.last().pose(), -32 + 64 * currentStunPercent, 8, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 1.0F).uv2(light).endVertex();
        stunBar.vertex(poseStack.last().pose(), -32 + 64 * currentStunPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 0.5F).uv2(light).endVertex();
*/    }
}
