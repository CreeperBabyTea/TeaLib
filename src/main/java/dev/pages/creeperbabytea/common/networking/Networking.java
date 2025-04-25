package dev.pages.creeperbabytea.common.networking;


import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Networking {
    protected PayloadRegistrar registrar;
    protected final ResourceLocation namespace;
    protected final String version;

    private final Collection<PacketProvider<?>> packets = new LinkedHashSet<>();
    private final Collection<Consumer<PayloadRegistrar>> rawPackets = new LinkedHashSet<>();

    public Networking(ResourceLocation namespace, String version) {
        this.namespace = namespace;
        this.version = version;
    }

    public void init(IEventBus mod, IEventBus game) {
        mod.addListener(this::register);
        this.packets.forEach(pp -> pp.init(mod, game));
    }

    @SafeVarargs
    public final <P extends CustomPacketPayload> void sendToServer(P packet, P... others) {
        PacketDistributor.sendToServer(packet, others);
    }

    public final <P extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, P packet, P... others) {
        PacketDistributor.sendToPlayer(player, packet, others);
    }

    public final <P extends CustomPacketPayload> void sendToAllPlayers(P packet, P... others) {
        PacketDistributor.sendToAllPlayers(packet, others);
    }

    public final <P extends CustomPacketPayload> void sendToAllPlayersInDimension(ServerLevel level, P packet, P... others) {
        PacketDistributor.sendToPlayersInDimension(level, packet, others);
    }

    public void registerPack(PacketProvider<?>... packets) {
        Collections.addAll(this.packets, packets);
    }

    @SafeVarargs
    public final void registerPack(Consumer<PayloadRegistrar>... packsIn) {
        Collections.addAll(rawPackets, packsIn);
    }

    private void register(RegisterPayloadHandlersEvent event) {
        this.registrar = event.registrar(version);

        packets.forEach(p -> p.register(registrar));
        rawPackets.forEach(p -> p.accept(registrar));
    }
}
