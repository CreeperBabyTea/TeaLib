package dev.pages.creeperbabytea.common.init;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.common.networking.listener.CInventoryChangeListener;
import dev.pages.creeperbabytea.common.networking.packet.CRawMouseInputPacket;
import dev.pages.creeperbabytea.common.networking.IPacket;
import dev.pages.creeperbabytea.common.networking.Networking;
import dev.pages.creeperbabytea.common.networking.PacketProvider;
import dev.pages.creeperbabytea.common.networking.packet.SInventoryChangePacket;
import dev.pages.creeperbabytea.common.networking.preparer.SInventoryChangePreparer;
import net.neoforged.bus.api.IEventBus;

public class Packets {
    private static final Networking INSTANCE = TeaLib.NETWORKING;

    public static final PacketProvider<CRawMouseInputPacket> cRAW_MOUSE_INPUT = register(CRawMouseInputPacket.PROVIDER);

    public static final PacketProvider<SInventoryChangePacket> sINVENTORY_CHANGE = register(SInventoryChangePacket.PROVIDER);

    public static void init(IEventBus mod, IEventBus game) {
        INSTANCE.init(mod, game);

        sINVENTORY_CHANGE.addClientListener(new CInventoryChangeListener());
        sINVENTORY_CHANGE.addServerPreparer(new SInventoryChangePreparer());
    }

    private static <P extends IPacket<P>> PacketProvider<P> register(PacketProvider<P> pp) {
        INSTANCE.registerPack(pp);
        return pp;
    }
}
