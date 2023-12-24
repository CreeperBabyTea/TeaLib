package creeperbabytea.tealib.common.network;

import creeperbabytea.tealib.TeaLib;
import creeperbabytea.tealib.client.operation.HotKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "0.0.1";
    private static int id = 0;

    protected static int nextId() {
        return id++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(TeaLib.modLocation("networking"), () -> VERSION, v -> v.equals(VERSION), v -> v.equals(VERSION));
        INSTANCE.messageBuilder(HotKey.KeyPressedPack.class, nextId()).encoder(HotKey.KeyPressedPack::encode).decoder(HotKey.KeyPressedPack::decode).consumer(HotKey.KeyPressedPack::handler).add();
    }

    public static void addListener(IEventBus mod) {
        mod.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(Networking::register));
    }
}
