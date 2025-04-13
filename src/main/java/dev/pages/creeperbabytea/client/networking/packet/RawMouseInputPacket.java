package dev.pages.creeperbabytea.client.networking.packet;

import com.mojang.datafixers.types.Type;
import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.client.util.event.InputEventHelper;
import dev.pages.creeperbabytea.common.networking.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.InputEvent;

public record RawMouseInputPacket(boolean isLeftClick, boolean isPress, int modifiers) implements IPacket<RawMouseInputPacket> {
    public static final ResourceLocation TYPE = TeaLib.modLoc("raw_mouse_input");
    public static final StreamCodec<ByteBuf, RawMouseInputPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            RawMouseInputPacket::isLeftClick,
            ByteBufCodecs.BOOL,
            RawMouseInputPacket::isPress,
            ByteBufCodecs.INT,
            RawMouseInputPacket::modifiers,
            RawMouseInputPacket::new
    );
    public static final PacketProvider<RawMouseInputPacket> PROVIDER = new PacketProvider.ToServer<>(TYPE, CODEC);

    public static RawMouseInputPacket of(InputEvent.MouseButton event) {
        return new RawMouseInputPacket(InputEventHelper.isLeftButton(event), InputEventHelper.isPress(event), event.getModifiers());
    }

    @Override
    public Type<RawMouseInputPacket> type() {
        return PROVIDER.getType();
    }

    @Override
    public PacketProvider<RawMouseInputPacket> provider() {
        return PROVIDER;
    }
}
