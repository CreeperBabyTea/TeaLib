package dev.pages.creeperbabytea.common.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 包装一个{@link IPacket}类，提供其注册和监听。<br>
 * 对于同一个IPacket类，其对应的PackerProvider应是单例的，且要在{@link IPacket#provider}中提供之。
 * PacketProvider提供三个子类，即<code>ToClient</code>、<code>ToServer</code>和<code>BiDirectional</code>。
 *
 * @see PacketPreparer
 * @see PacketListener
 * @see IPacket
 * @see Networking
 */
public abstract class PacketProvider<P extends IPacket<P>> {
    protected final CustomPacketPayload.Type<P> type;
    protected final StreamCodec<? super RegistryFriendlyByteBuf, P> codec;
    protected final boolean cancelable;

    protected List<PacketListener<P>> clientListeners, serverListeners;
    protected List<PacketPreparer<P>> clientPreparers, serverPreparers;

    protected PacketProvider(ResourceLocation key, StreamCodec<? super RegistryFriendlyByteBuf, P> codec, boolean cancelable) {
        this.type = new CustomPacketPayload.Type<>(key);
        this.codec = codec;
        this.cancelable = cancelable;
    }

    /**
     * 添加客户端接收监听器。
     * @throws NullPointerException 如果对ToServer使用。
     */
    @SafeVarargs
    public final void addClientListener(PacketListener<P>... listeners) {
        Collections.addAll(this.clientListeners, listeners);
    }

    /**
     * 添加服务端接收监听器。
     * @throws NullPointerException 如果对ToClient使用。
     */
    @SafeVarargs
    public final void addServerListener(PacketListener<P>... listeners) {
        Collections.addAll(this.serverListeners, listeners);
    }

    /**
     * 添加客户端发送监听器。
     * @throws NullPointerException 如果对ToClient使用。
     */
    @SafeVarargs
    public final void addClientPreparer(PacketPreparer<P>... preparers) {
        Collections.addAll(this.clientPreparers, preparers);
    }

    /**
     * 添加服务端发送监听器。
     * @throws NullPointerException 如果对ToServer使用。
     */
    @SafeVarargs
    public final void addServerPreparer(PacketPreparer<P>... preparers) {
        Collections.addAll(this.serverPreparers, preparers);
    }

    public abstract void register(final PayloadRegistrar registrar);

    public CustomPacketPayload.Type<P> getType() {
        return type;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, P> getCodec() {
        return codec;
    }

    /*----------客户端，启动！----------*/

    @OnlyIn(Dist.CLIENT)
    private void maybeSendToServer(@Nullable LocalPlayer player, P packet) {
        if (!this.cancelable || clientPreparers.stream().allMatch(preparer -> preparer.maybeCall(player, packet)))
            PacketDistributor.sendToServer(packet);
    }

    @OnlyIn(Dist.CLIENT)
    public final void sendToServer(P packet) {
        var localPlayer = Minecraft.getInstance().player;
        maybeSendToServer(localPlayer, packet);
    }

    /*----------服务端，启动！----------*/

    @OnlyIn(Dist.DEDICATED_SERVER)
    private void maybeSendToClient(ServerPlayer player, P packet) {
        if (!this.cancelable || serverPreparers.stream().allMatch(preparer -> preparer.maybeCall(player, packet)))
            PacketDistributor.sendToPlayer(player, packet);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public final void sendToPlayer(ServerPlayer player, P packet) {
        maybeSendToClient(player, packet);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public final void sendToAllPlayers(P packet) {
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        server.getPlayerList().getPlayers().forEach(player -> maybeSendToClient(player, packet));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public final void sendToAllPlayersInDimension(ServerLevel level, P packet) {
        level.players().forEach(player -> maybeSendToClient(player, packet));
    }

    public static final class BiDirectional<P extends IPacket<P>> extends PacketProvider<P> {
        public BiDirectional(ResourceLocation key, StreamCodec<? super RegistryFriendlyByteBuf, P> codec, boolean cancelable) {
            super(key, codec, cancelable);
            this.clientListeners = new ArrayList<>();
            this.serverListeners = new ArrayList<>();
            this.clientPreparers = new ArrayList<>();
            this.serverPreparers = new ArrayList<>();
        }

        @Override
        public void register(PayloadRegistrar registrar) {
            registrar.playBidirectional(type, codec, new DirectionalPayloadHandler<>(
                    (packet, ctx) -> clientListeners.forEach(l -> l.maybeCall(packet, ctx)),
                    (packet, ctx) -> serverListeners.forEach(l -> l.maybeCall(packet, ctx))
            ));
        }
    }

    public static final class ToServer<P extends IPacket<P>> extends PacketProvider<P> {
        public ToServer(ResourceLocation key, StreamCodec<? super RegistryFriendlyByteBuf, P> codec, boolean cancelable) {
            super(key, codec, cancelable);
            this.serverListeners = new ArrayList<>();
            this.clientPreparers = new ArrayList<>();
        }

        @Override
        public void register(PayloadRegistrar registrar) {
            registrar.playToServer(type, codec, (packet, ctx) -> serverListeners.forEach(l -> l.maybeCall(packet, ctx)));
        }
    }

    public static final class ToClient<P extends IPacket<P>> extends PacketProvider<P> {
        public ToClient(ResourceLocation key, StreamCodec<? super RegistryFriendlyByteBuf, P> codec, boolean cancelable) {
            super(key, codec, cancelable);
            this.clientListeners = new ArrayList<>();
            this.serverPreparers = new ArrayList<>();
        }

        @Override
        public void register(PayloadRegistrar registrar) {
            registrar.playToClient(type, codec, (packet, ctx) -> clientListeners.forEach(l -> l.maybeCall(packet, ctx)));
        }
    }
}
