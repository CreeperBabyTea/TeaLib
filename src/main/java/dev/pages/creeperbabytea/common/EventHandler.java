package dev.pages.creeperbabytea.common;

import dev.pages.creeperbabytea.common.register.Register;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public class EventHandler {
    public static void init(IEventBus mod, IEventBus game) {
        mod.addListener(EventPriority.LOWEST, EventHandler::afterRegister);
    }


    public static void afterRegister(RegisterEvent event) {
        Register.clearUp();
    }
}
