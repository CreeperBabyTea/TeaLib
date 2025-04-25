package dev.pages.creeperbabytea;

import com.mojang.logging.LogUtils;
import dev.pages.creeperbabytea.client.ClientEventHandler;
import dev.pages.creeperbabytea.common.EventHandler;
import dev.pages.creeperbabytea.common.networking.Networking;
import dev.pages.creeperbabytea.common.init.Packets;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(TeaLib.MODID)
public class TeaLib {
    public static final String MODID = "tea_lib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String VERSION = "0.0.1";

    public static IEventBus MOD;
    public static IEventBus GAME;

    public static Networking NETWORKING;

    public TeaLib(IEventBus modEventBus, ModContainer modContainer) {
        MOD = modEventBus;
        GAME = NeoForge.EVENT_BUS;
        NETWORKING = new Networking(modLoc("main"), VERSION);

        Packets.init(MOD, GAME);
        EventHandler.init(MOD, GAME);
        ClientEventHandler.init(GAME);
    }


    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation forgeLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("forge", path);
    }

    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", path);
    }
}
