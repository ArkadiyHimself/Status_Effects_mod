package net.arkadiyhimself.statuseffects.world.StunRender;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.arkadiyhimself.statuseffects.StatusEffects;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
public class StunBar extends RenderStateShard {
    public static final ResourceLocation STUN_BAR = new ResourceLocation(StatusEffects.MODID, "textures/above_entities/stun_bar.png");
    public static final RenderType BAR_TEXTURE_TYPE = emptyStunBarType();
    public StunBar(String pName, Runnable pSetupState, Runnable pClearState) {super(pName, pSetupState, pClearState);}
    private static RenderType emptyStunBarType() {
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setTextureState(new TextureStateShard(StunBar.STUN_BAR, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .createCompositeState(false);
        return StunBar.SE_create("stun_bar", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true, renderTypeState);
    }
    private static RenderType SE_create(String name, VertexFormat format, VertexFormat.Mode mode, int bufSize, boolean affectsCrumbling, boolean sortOnUpload, RenderType.CompositeState glState) {
        return RenderType.create(name, format, mode, bufSize, affectsCrumbling, sortOnUpload, glState);
    }
}
