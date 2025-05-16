package dev.pages.creeperbabytea.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public abstract class BarLikeLayer extends PlayerStateLayer {
    protected final ResourceLocation backgroundSprite;
    protected final ResourceLocation progressSprite;
    protected int width;
    protected int height;

    /**
     * @param value 用于填充进度
     */
    protected BarLikeLayer(ResourceLocation id, Function<Player, Float> value, ResourceLocation backgroundSprite, ResourceLocation progressSprite, int width, int height) {
        this(id, value, backgroundSprite, progressSprite);
        this.width = width;
        this.height = height;
    }

    /**
     * 记得设置宽度和高度
     */
    protected BarLikeLayer(ResourceLocation id, Function<Player, Float> value, ResourceLocation backgroundSprite, ResourceLocation progressSprite) {
        super(id, value);
        this.backgroundSprite = backgroundSprite;
        this.progressSprite = progressSprite;
    }

    @Override
    protected void onAdd2Wrapper(LayerWrapper wrapper) {
        wrapper.addAboveHotbarLeftHeightDefault(getAdditionHeight());
        wrapper.addAboveHotbarRightHeightDefault(getAdditionHeight());
    }

    @Override
    public float getValue(Player player) {
        return Math.clamp(super.getValue(player), 0, 1);
    }

    protected void renderBarBackground(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value) {
        guiGraphics.blitSprite(RenderType::guiTextured, this.getBackgroundSprite(), xOffset, yOffset, width, height);
    }

    protected void renderBarProgress(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value) {
        guiGraphics.blitSprite(RenderType::guiTextured, this.getProgressSprite(), width, height, 0, 0, xOffset, yOffset, Math.round(width * value), 5);
    }

    protected int getAdditionHeight() {
        return this.height + 2;
    }

    protected ResourceLocation getBackgroundSprite() {
        return backgroundSprite;
    }

    protected ResourceLocation getProgressSprite() {
        return progressSprite;
    }
}
