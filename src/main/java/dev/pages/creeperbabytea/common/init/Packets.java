package dev.pages.creeperbabytea.common.init;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.client.networking.packet.CRawMouseInputPacket;
import dev.pages.creeperbabytea.common.networking.Networking;
import dev.pages.creeperbabytea.common.networking.PacketProvider;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;

public class Packets {
    private static final Networking INSTANCE = TeaLib.NETWORKING;

    public static final PacketProvider<CRawMouseInputPacket> RAW_MOUSE_INPUT_PACKET_PACKET_PROVIDER = register(CRawMouseInputPacket.PROVIDER);

    public static void init(IEventBus mod, IEventBus game) {
        INSTANCE.init(mod, game);
    }

    private static <P extends CustomPacketPayload> PacketProvider<P> register(PacketProvider<P> pp) {
        INSTANCE.registerPack(pp);
        return pp;
    }
}
