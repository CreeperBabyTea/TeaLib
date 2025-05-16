package dev.pages.creeperbabytea.common.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;

/**
 * 在<strong>发送端</strong>的发送监听器。<br>
 * 通过{@link PacketProvider#addClientListener}以及{@link PacketProvider#addServerListener}添加
 *
 * @see PacketListener
 * @see PacketProvider
 */
public abstract class PacketPreparer<P extends CustomPacketPayload> {
    /**
     * 是否可以取消<strong>监听行为</strong>，与是否可以取消<strong>发包</strong>无关。<br>
     * 是否可以取消发包取决于{@link PacketProvider#cancelable}。
     */
    private boolean isCancelable;

    private BiPredicate<Player, P> cancelPredicate;

    public PacketPreparer(boolean isCancelable) {
        if (isCancelable)
            this.cancelPredicate = (qwq, ovo) -> false;
    }

    public PacketPreparer() {
        this(false);
    }

    final boolean maybeCall(@Nullable Player player, final P packet) {
        if (this.isCancelable && cancelPredicate.test(player, packet))
            return true;
        return call(player, packet);
    }

    /**
     * @return <code>true</code>如果正常发包。<br>
     * 如果注册到的包不允许取消发包，则该结果被忽略。<br>
     * <strong>在取消发包前请谨慎考虑，这可能导致服务端与客户端不同步</strong>
     */
    public abstract boolean call(@Nullable Player player, final P packet);

    public void setCancelOn(BiPredicate<Player, P> appendedCondition) {
        if (!this.isCancelable)
            throw new IllegalStateException("Can't cancel a listener that's not cancelable: " + this);

        this.cancelPredicate = this.cancelPredicate.or(appendedCondition);
    }
}
