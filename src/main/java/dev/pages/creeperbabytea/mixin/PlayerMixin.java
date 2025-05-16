package dev.pages.creeperbabytea.mixin;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.common.event.player.MainHandItemChangedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin {
    @Shadow
    private ItemStack lastItemInMainHand;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack injectTick(ItemStack instance) {
        var ret = instance.copy();
        TeaLib.GAME.post(new MainHandItemChangedEvent((Player)(Object) this, lastItemInMainHand.copy(), ret));
        return ret;
    }
}
