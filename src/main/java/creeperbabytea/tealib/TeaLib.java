package creeperbabytea.tealib;

import creeperbabytea.tealib.client.operation.HotKey;
import creeperbabytea.tealib.client.operation.HotKeyListener;
import creeperbabytea.tealib.common.network.Networking;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

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

        Networking.addListener(MOD);
    }


    public static ResourceLocation modLocation(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static ResourceLocation forgeLocation(String path) {
        return new ResourceLocation("forge", path);
    }

    public static ResourceLocation mcLocation(String path) {
        return new ResourceLocation("minecraft", path);
    }
}
