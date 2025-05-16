package dev.pages.creeperbabytea.common.networking.preparer;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.common.event.player.InventoryChangeEvent;
import dev.pages.creeperbabytea.common.networking.PacketPreparer;
import dev.pages.creeperbabytea.common.networking.packet.SInventoryChangePacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SInventoryChangePreparer extends PacketPreparer<SInventoryChangePacket> {
    @Override
    public boolean call(@Nullable Player player, SInventoryChangePacket packet) {
        Objects.requireNonNull(player);
        TeaLib.GAME.post(new InventoryChangeEvent(player, packet.full(), packet.empty(), packet.occupied()));
        return true;
    }
}
