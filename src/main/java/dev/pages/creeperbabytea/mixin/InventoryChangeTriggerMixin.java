package dev.pages.creeperbabytea.mixin;

import dev.pages.creeperbabytea.common.init.Packets;
import dev.pages.creeperbabytea.common.networking.packet.SInventoryChangePacket;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin {
    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)V", at = @At("RETURN"))
    private void injectTrigger(ServerPlayer player, Inventory inventory, ItemStack stack, int full, int empty, int occupied, CallbackInfo ci) {
        Packets.sINVENTORY_CHANGE.sendToPlayer(player, new SInventoryChangePacket(full, empty, occupied));
    }
}
