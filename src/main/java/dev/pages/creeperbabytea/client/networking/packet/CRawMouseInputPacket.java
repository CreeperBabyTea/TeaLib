package dev.pages.creeperbabytea.client.networking.packet;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.client.util.event.InputEventHelper;
import dev.pages.creeperbabytea.common.networking.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.InputEvent;

public record CRawMouseInputPacket(boolean isLeftClick, boolean isPress, int modifiers) implements IPacket<CRawMouseInputPacket> {
    public static final ResourceLocation TYPE = TeaLib.modLoc("raw_mouse_input");
    public static final StreamCodec<ByteBuf, CRawMouseInputPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            CRawMouseInputPacket::isLeftClick,
            ByteBufCodecs.BOOL,
            CRawMouseInputPacket::isPress,
            ByteBufCodecs.INT,
            CRawMouseInputPacket::modifiers,
            CRawMouseInputPacket::new
    );
    public static final PacketProvider<CRawMouseInputPacket> PROVIDER = new PacketProvider.ToServer<>(TYPE, STREAM_CODEC);

    public static CRawMouseInputPacket of(InputEvent.MouseButton event) {
        return new CRawMouseInputPacket(InputEventHelper.isLeftButton(event), InputEventHelper.isPress(event), event.getModifiers());
    }

    @Override
    public Type<CRawMouseInputPacket> type() {
        return PROVIDER.getType();
    }

    @Override
    public PacketProvider<CRawMouseInputPacket> provider() {
        return PROVIDER;
    }
}
