package dev.pages.creeperbabytea.common.event.player;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * 当玩家背包改变时，同时在服务端和客户端的<code>game</code>总线发布。
 */
public class InventoryChangeEvent extends PlayerEvent {
    private final Inventory inventory;
    private final int full;
    private final int empty;
    private final int occupied;

    public InventoryChangeEvent(Player player, int full, int empty, int occupied) {
        super(player);
        this.inventory = player.getInventory();
        this.full = full;
        this.empty = empty;
        this.occupied = occupied;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getFullSlots() {
        return full;
    }

    public int getEmptySlots() {
        return empty;
    }

    public int getOccupiedSlots() {
        return occupied;
    }

    @Override
    public String toString() {
        return "InventoryChangeEvent{" +
                "inventory=" + inventory +
                ", full=" + full +
                ", empty=" + empty +
                ", occupied=" + occupied +
                '}';
    }
}
