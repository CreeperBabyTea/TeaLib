package dev.pages.creeperbabytea.common.networking.listener;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.common.event.player.InventoryChangeEvent;
import dev.pages.creeperbabytea.common.networking.PacketListener;
import dev.pages.creeperbabytea.common.networking.packet.SInventoryChangePacket;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public class CInventoryChangeListener extends PacketListener<SInventoryChangePacket> {
    @Override
    public void call(SInventoryChangePacket packet, IPayloadContext ctx) {
        var localPlayer = Objects.requireNonNull(Minecraft.getInstance().player, "Unexpected inventory change packet before joining game.");
        TeaLib.GAME.post(new InventoryChangeEvent(localPlayer, packet.full(), packet.empty(), packet.occupied()));
    }
}
