package dev.pages.creeperbabytea.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

/**
 * 在物品栏左右侧渲染50*5 尺寸的进度条状ui
 */
public class HotbarSideBarLayer extends BarLikeLayer {
    private final boolean isOnLeftSide;
    private final int dxOffset;

    protected HotbarSideBarLayer(ResourceLocation id, Function<Player, Float> value, ResourceLocation backgroundSprite, ResourceLocation progressSprite, boolean isOnLeftSide) {
        super(id, value, backgroundSprite, progressSprite, 50, 5);
        this.isOnLeftSide = isOnLeftSide;
        this.dxOffset = isOnLeftSide ? -151 : 101;
    }

    @Override
    protected void onAdd2Wrapper(LayerWrapper wrapper) {
    }

    @Override
    protected void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player) {
        int x = guiGraphics.guiWidth() / 2 + dxOffset;
        int y = guiGraphics.guiHeight() - getAdditionHeight();
        float value = getValue(player);

        renderBarBackground(guiGraphics, deltaTracker, player, x, y, value);
        renderBarProgress(guiGraphics, deltaTracker, player, x, y, value);

        if (isOnLeftSide)
            LayerWrapper.addBesideHotbarLeft(getAdditionHeight());
        else
            LayerWrapper.addBesideHotbarRight(getAdditionHeight());
    }
}
