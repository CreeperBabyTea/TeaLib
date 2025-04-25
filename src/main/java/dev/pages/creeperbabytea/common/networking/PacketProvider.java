package dev.pages.creeperbabytea.common.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PacketProvider<P extends CustomPacketPayload> {
    protected final CustomPacketPayload.Type<P> type;
    protected final StreamCodec<ByteBuf, P> codec;
    protected List<PacketListener<P>> clientListeners, serverListeners;

    protected PacketProvider(ResourceLocation key, StreamCodec<ByteBuf, P> codec) {
        this.type = new CustomPacketPayload.Type<>(key);
        this.codec = codec;
    }

    @SafeVarargs
    public final void addClientListener(PacketListener<P>... listeners) {
        Collections.addAll(this.clientListeners, listeners);
    }

    @SafeVarargs
    public final void addServerListener(PacketListener<P> listener, PacketListener<P>... others) {
        serverListeners.add(listener);
        Collections.addAll(this.serverListeners, others);
    }

    public abstract void register(final PayloadRegistrar registrar);

    public CustomPacketPayload.Type<P> getType() {
        return type;
    }

    public StreamCodec<ByteBuf, P> getCodec() {
        return codec;
    }

    public abstract void init(IEventBus mod, IEventBus game);

    public static class BiDirectional<P extends CustomPacketPayload> extends PacketProvider<P> {
        public BiDirectional(ResourceLocation key, StreamCodec<ByteBuf, P> codec) {
            super(key, codec);
            this.clientListeners = new ArrayList<>();
            this.serverListeners = new ArrayList<>();
        }

        @Override
        public void register(PayloadRegistrar registrar) {
            registrar.playBidirectional(type, codec, new DirectionalPayloadHandler<>(
                    (packet, ctx) -> clientListeners.forEach(l -> l.onPacketReceived(packet, ctx)),
                    (packet, ctx) -> serverListeners.forEach(l -> l.onPacketReceived(packet, ctx))
            ));
        }

        @Override
        public void init(IEventBus mod, IEventBus game) {
            clientListeners.forEach(l -> l.init(mod, game));
            serverListeners.forEach(l -> l.init(mod, game));
        }
    }

    public static class ToServer<P extends CustomPacketPayload> extends PacketProvider<P> {
        public ToServer(ResourceLocation key, StreamCodec<ByteBuf, P> codec) {
            super(key, codec);
            this.serverListeners = new ArrayList<>();
        }

        @Override
        public void register(PayloadRegistrar registrar) {
            registrar.playToServer(type, codec, (packet, ctx) -> serverListeners.forEach(l -> l.onPacketReceived(packet, ctx)));
        }

        @Override
        public void init(IEventBus mod, IEventBus game) {
            serverListeners.forEach(l -> l.init(mod, game));
        }
    }

    public static class ToClient<P extends CustomPacketPayload> extends PacketProvider<P> {
        public ToClient(ResourceLocation key, StreamCodec<ByteBuf, P> codec) {
            super(key, codec);
            this.clientListeners = new ArrayList<>();
        }

        @Override
        public void register(PayloadRegistrar registrar) {
            registrar.playToClient(type, codec, (packet, ctx) -> clientListeners.forEach(l -> l.onPacketReceived(packet, ctx)));
        }

        @Override
        public void init(IEventBus mod, IEventBus game) {
            clientListeners.forEach(l -> l.init(mod, game));
        }
    }
}
