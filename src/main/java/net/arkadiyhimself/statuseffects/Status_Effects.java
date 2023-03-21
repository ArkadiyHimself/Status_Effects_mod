package net.arkadiyhimself.statuseffects;

import com.mojang.logging.LogUtils;
import net.arkadiyhimself.statuseffects.blocks.Modblocks;
import net.arkadiyhimself.statuseffects.effects.ModMobEffect;
import net.arkadiyhimself.statuseffects.items.Moditems;
import net.arkadiyhimself.statuseffects.sound.ModSounds;
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
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;

@Mod(Status_Effects.MODID)
public class Status_Effects
{
    public static final String MODID = "statuseffects";
    private static final Logger LOGGER = LogUtils.getLogger();
    public Status_Effects()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModMobEffect.register(modEventBus);
        Moditems.register(modEventBus);
        Modblocks.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
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
