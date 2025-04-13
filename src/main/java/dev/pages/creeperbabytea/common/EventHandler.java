package dev.pages.creeperbabytea.common;

import dev.pages.creeperbabytea.common.register.Register;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

public class EventHandler {
    public static void init(IEventBus mod, IEventBus forge) {
        mod.addListener(EventHandler::onServerStarted);
    }


    public static void onServerStarted(ServerStartedEvent event) {
        Register.clearUp();
    }
}
