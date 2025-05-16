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
import java.util.function.Consumer;

public class Networking {
    protected PayloadRegistrar registrar;
    protected final ResourceLocation namespace;
    protected final String version;

    private final Collection<PacketProvider<?>> packets = new LinkedHashSet<>();

    public Networking(ResourceLocation namespace, String version) {
        this.namespace = namespace;
        this.version = version;
    }

    public void init(IEventBus mod, IEventBus game) {
        mod.addListener(this::register);
    }

    private void register(RegisterPayloadHandlersEvent event) {
        this.registrar = event.registrar(version);
        packets.forEach(p -> p.register(registrar));
    }

    public void registerPack(PacketProvider<?>... packets) {
        Collections.addAll(this.packets, packets);
    }
}
