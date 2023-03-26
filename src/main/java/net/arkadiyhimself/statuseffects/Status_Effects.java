package net.arkadiyhimself.statuseffects;

import com.mojang.logging.LogUtils;
import net.arkadiyhimself.statuseffects.blocks.SE_Blocks;
import net.arkadiyhimself.statuseffects.effects.SE_MobEffect;
import net.arkadiyhimself.statuseffects.items.SE_ModItem;
import net.arkadiyhimself.statuseffects.networking.Messages;
import net.arkadiyhimself.statuseffects.particles.SE_Particles;
import net.arkadiyhimself.statuseffects.sound.SE_Sounds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

@Mod(Status_Effects.MODID)
public class Status_Effects
{
    public static final String MODID = "statuseffects";
    private static final Logger LOGGER = LogUtils.getLogger();
    public Status_Effects()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SE_MobEffect.register(modEventBus);
        SE_ModItem.register(modEventBus);
        SE_Blocks.register(modEventBus);
        SE_Sounds.register(modEventBus);
        SE_Particles.register(modEventBus);

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
