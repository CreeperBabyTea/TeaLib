package dev.pages.creeperbabytea.common.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.BiPredicate;

/**
 * 在<strong>接收端</strong>的监听器。<br>
 * 通过{@link PacketProvider#addClientListener}以及{@link PacketProvider#addServerListener}添加
 *
 * @see PacketPreparer
 * @see PacketProvider
 */
public abstract class PacketListener<P extends CustomPacketPayload> {
    private final boolean isCancelable;

    private BiPredicate<P, IPayloadContext> cancelPredicate;

    public PacketListener(boolean isCancelable) {
        this.isCancelable = isCancelable;

        if (isCancelable)
            this.cancelPredicate = (p, ctxSup) -> false;
    }

    public PacketListener() {
        this(false);
    }

    final void maybeCall(P packet, IPayloadContext ctx) {
        if (this.isCancelable && cancelPredicate.test(packet, ctx))
            return;
        call(packet, ctx);
    }

    public abstract void call(P packet, IPayloadContext ctx);

    public void setCancelOn(BiPredicate<P, IPayloadContext> appendedCondition) {
        if (!this.isCancelable)
            throw new IllegalStateException("Can't cancel a listener that's not cancelable: " + this);

        this.cancelPredicate = this.cancelPredicate.or(appendedCondition);
    }
}
