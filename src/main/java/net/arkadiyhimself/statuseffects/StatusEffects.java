package net.arkadiyhimself.statuseffects;

import com.mojang.logging.LogUtils;
import net.arkadiyhimself.statuseffects.attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.blocks.StatusEffectsBlocks;
import net.arkadiyhimself.statuseffects.capability.DisarmEffect.DisarmEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.FreezeEffect.FreezeEffectAttacher;
import net.arkadiyhimself.statuseffects.capability.StunEffect.StunEffectAttacher;
import net.arkadiyhimself.statuseffects.items.StatusEffectsModItem;
import net.arkadiyhimself.statuseffects.networking.NetworkHandler;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.sound.SoundWhispers;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
import net.arkadiyhimself.statuseffects.mobeffects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.sound.SwordClashSounds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(StatusEffects.MODID)
public class StatusEffects
{
    public static final String MODID = "statuseffects";
    private static final Logger LOGGER = LogUtils.getLogger();
    public StatusEffects()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        StatusEffectsMobEffect.register(modEventBus);
        StatusEffectsModItem.register(modEventBus);
        StatusEffectsBlocks.register(modEventBus);
        StatusEffectsParticles.register(modEventBus);
        StatusEffectsAttributes.register(modEventBus);

        // sounds
        StatusEffectsSounds.register(modEventBus);
        SoundWhispers.register(modEventBus);
        SwordClashSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        // capabilities
        StunEffectAttacher.register();
        FreezeEffectAttacher.register();
        DisarmEffectAttacher.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
//        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS)
//            event.accept(EXAMPLE_BLOCK_ITEM);
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
