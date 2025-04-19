package dev.pages.creeperbabytea.mixin;

import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.client.gui.LayerWrapper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.gui.GuiLayerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("all")
@Mixin(Gui.class)
public abstract class GuiAccessor {
    @Accessor("leftHeight")
    abstract void setLeftHeight(int val);

    @Accessor("rightHeight")
    abstract void setRightHeight(int val);

    @Accessor("layerManager")
    abstract GuiLayerManager getLayerManager();

    /*@ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 0)
    private GuiLayerManager modifyHealthBar(GuiLayerManager playerHealthComponents) {
        return new GuiLayerManager();
    }*/

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        setLeftHeight(LayerWrapper.leftHeight);
        setRightHeight(LayerWrapper.rightHeight);
        getLayerManager().render(graphics, deltaTracker);
        LayerWrapper.reload();
        ci.cancel();
    }

    @ModifyVariable(method = "renderExperienceLevel", at = @At("STORE"), ordinal = 2)
    private int modifyXpLevelHeight(int heightIn) {
        return heightIn + 4;
    }
}
