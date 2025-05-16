package dev.pages.creeperbabytea.common.networking;

import dev.pages.creeperbabytea.common.networking.packet.CRawMouseInputPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * 一个包装了{@link PacketProvider}的网络包。<br>
 * 网络包的类应是<Strong>记录</Strong>，且任何字段<strong>不应该</strong>允许修改，以防发包前后在两端处理的数据不同步。<br>
 *
 * @see PacketProvider
 * @see Networking
 * @see CRawMouseInputPacket
 */
public interface IPacket<P extends IPacket<P>> extends CustomPacketPayload {
    PacketProvider<P> provider();
}
