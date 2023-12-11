package creeperbabytea.tealib;

import creeperbabytea.tealib.common.objects.Block_ItemCollection;
import creeperbabytea.tealib.common.objects.SingleItemEntry;
import creeperbabytea.tealib.registry.GeneralDeferredRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TeaLib.MODID)
public class TeaLib {
    public static final String MODID = "tea_lib";
    public static final Logger LOGGER = LogManager.getLogger();

    public static TeaLib INSTANCE;

    public static IEventBus MOD;
    public static IEventBus FORGE;

    public TeaLib() {
        INSTANCE = this;

        MOD = FMLJavaModLoadingContext.get().getModEventBus();
        FORGE = MinecraftForge.EVENT_BUS;

        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }
}
