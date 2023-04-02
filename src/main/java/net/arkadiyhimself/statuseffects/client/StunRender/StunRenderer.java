package net.arkadiyhimself.statuseffects.client.StunRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.arkadiyhimself.statuseffects.capability.StunScaleAttacher;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

import java.util.function.Supplier;

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
        poseStack.pushPose();
        poseStack.popPose();
        poseStack.scale(-globalScale, -globalScale, -globalScale);

        VertexConsumer stunBar = buffers.getBuffer(StunBar.BAR_TEXTURE_TYPE);

        // this bar is rendering before entity is stunned by your hits
        StunScaleAttacher.getStunScale(living).ifPresent(stunScale -> {
            if (stunScale.getStunPoints() > 0 || stunScale.getStunned()) {
                int emptyR = 255;
                int emptyG = 255;
                int emptyB = 255;
                // empty bar
                stunBar.vertex(poseStack.last().pose(), -32, 0, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
                stunBar.vertex(poseStack.last().pose(), -32, 8, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                stunBar.vertex(poseStack.last().pose(), 32, 8, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(1.0F, 0.5F).uv2(light).endVertex();
                stunBar.vertex(poseStack.last().pose(), 32, 0, 0.001F).color(emptyR, emptyG, emptyB, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
                // filling
                if (!stunScale.getStunned()) {
                    // renders when entity isn't stunned yet
                    double pointsBeforeStunned = stunScale.getMaxStunPoints();
                    double currentStunPoints = stunScale.getStunPoints();
                    float currentStunPercent = (float) (currentStunPoints / pointsBeforeStunned);
                    int r = (int) Math.ceil(255 * currentStunPercent);
                    int g = 255;
                    int b = 255;
                    stunBar.vertex(poseStack.last().pose(), -32, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32, 8, 0.002F).color(r, g, b, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32 + 64 * currentStunPercent, 8, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32 + 64 * currentStunPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunPercent, 0.5F).uv2(light).endVertex();
                } else {
                    // renders when entity is stunned
                    double initDuration = stunScale.getStunDurationInitial();
                    double currentDuration = stunScale.getCurrentDuration();
                    float currentStunDurationPercent = (float) (currentDuration / initDuration);
                    int r = (int) Math.ceil(255 * currentStunDurationPercent);
                    int g = 255;
                    int b = 255;
                    stunBar.vertex(poseStack.last().pose(), -32, 0, 0.002F).color(r, g, b, 255).uv(0.0F, 0.5F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32, 8, 0.002F).color(r, g, b, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32 + 64 * currentStunDurationPercent, 8, 0.002F).color(r, g, b, 255).uv(currentStunDurationPercent, 1.0F).uv2(light).endVertex();
                    stunBar.vertex(poseStack.last().pose(), -32 + 64 * currentStunDurationPercent, 0, 0.002F).color(r, g, b, 255).uv(currentStunDurationPercent, 0.5F).uv2(light).endVertex();

                }
            }
        });
    }
}
