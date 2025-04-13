package dev.pages.creeperbabytea.common.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface IPacket<P extends CustomPacketPayload> extends CustomPacketPayload {
    PacketProvider<P> provider();
}
