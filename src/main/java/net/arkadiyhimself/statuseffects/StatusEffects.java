package net.arkadiyhimself.statuseffects;

import com.mojang.logging.LogUtils;
import net.arkadiyhimself.statuseffects.Attributes.StatusEffectsAttributes;
import net.arkadiyhimself.statuseffects.blocks.StatusEffectsBlocks;
import net.arkadiyhimself.statuseffects.effects.StatusEffectsMobEffect;
import net.arkadiyhimself.statuseffects.items.StatusEffectsModItem;
import net.arkadiyhimself.statuseffects.networking.Messages;
import net.arkadiyhimself.statuseffects.particles.StatusEffectsParticles;
import net.arkadiyhimself.statuseffects.sound.StatusEffectsSounds;
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
        StatusEffectsSounds.register(modEventBus);
        StatusEffectsParticles.register(modEventBus);
        StatusEffectsAttributes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Messages.register();
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
