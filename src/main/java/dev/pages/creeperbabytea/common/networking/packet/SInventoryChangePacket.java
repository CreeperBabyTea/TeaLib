package dev.pages.creeperbabytea.common.networking.packet;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.common.networking.IPacket;
import dev.pages.creeperbabytea.common.networking.PacketProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public record SInventoryChangePacket(int full, int empty, int occupied) implements IPacket<SInventoryChangePacket> {
    public static final ResourceLocation TYPE = TeaLib.modLoc("inventory_change");
    public static final StreamCodec<RegistryFriendlyByteBuf, SInventoryChangePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SInventoryChangePacket::full,
            ByteBufCodecs.INT,
            SInventoryChangePacket::empty,
            ByteBufCodecs.INT,
            SInventoryChangePacket::occupied,
            SInventoryChangePacket::new
    );
    public static final PacketProvider<SInventoryChangePacket> PROVIDER = new PacketProvider.ToClient<>(TYPE, STREAM_CODEC, false);

    @Override
    public PacketProvider<SInventoryChangePacket> provider() {
        return PROVIDER;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PROVIDER.getType();
    }
}
