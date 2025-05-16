package dev.pages.creeperbabytea.client;

import dev.pages.creeperbabytea.common.networking.packet.CRawMouseInputPacket;
import dev.pages.creeperbabytea.common.init.Packets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.InputEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    private static final Supplier<LocalPlayer> clientPlayer = () -> Minecraft.getInstance().player;
    private static final Supplier<Screen> screen = () -> Minecraft.getInstance().screen;

    public static void init(IEventBus forge) {
        forge.addListener(ClientEventHandler::onMouseInput);
    }

    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (clientPlayer.get() != null && screen.get() == null)
            Packets.cRAW_MOUSE_INPUT.sendToServer(CRawMouseInputPacket.of(event));
    }
}
