package dev.pages.creeperbabytea.common.event.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * 玩家主手物品改变时，同时在服务端和客户端的<code>game</code>总线发布。
 */
public class MainHandItemChangedEvent extends PlayerEvent {
    private final ItemStack from, to;

    public MainHandItemChangedEvent(Player player, ItemStack from, ItemStack to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    public ItemStack getFrom() {
        return from;
    }

    public ItemStack getTo() {
        return to;
    }
}
