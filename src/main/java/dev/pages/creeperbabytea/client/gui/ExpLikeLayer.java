package dev.pages.creeperbabytea.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.Function;

public class ExpLikeLayer extends PlayerStateLayer {
    private final Function<Player, Float> fillProgress;
    private final int textColor;
    private final ResourceLocation background;
    private final ResourceLocation progress;
    protected ExpLikeLayer leftSibling;
    protected ExpLikeLayer rightSibling;
    protected int siblingAmount;
    private int index = 0;
    protected int dxOffset;
    protected int width;

    /**
     * @param rightSibling 可以在一行内添加多个ui。通过sibling添加的ui不需要再单独注册到{@link LayerWrapper}中
     */
    protected ExpLikeLayer(ResourceLocation id, Function<Player, Float> value, Function<Player, Float> fillProgress, int textColor, ResourceLocation background, ResourceLocation progress, @Nullable ExpLikeLayer rightSibling) {
        super(id, value, mc -> mc.gameMode != null && mc.gameMode.canHurtPlayer());
        this.fillProgress = fillProgress;
        this.textColor = textColor;
        this.background = background;
        this.progress = progress;
        if (rightSibling != null) {
            this.rightSibling = rightSibling.setIndex(this.index + 1);
            this.rightSibling.leftSibling = this;
        }
        if (this.index == 0) {
            siblingAmount = getSiblingAmount();
            this.dxOffset = -91;
            this.width = (186 - siblingAmount * 4) / siblingAmount;
        } else {
            switch (siblingAmount) {
                case 2:
                    this.width = 87;
                    break;
                case 3:
                    this.width = 58;
                    break;
                case 4:
                    this.width = (index == 1 || index == 2 ? 43 : 42);
                    break;
                case 5:
                    this.width = index == 2 ? 34 : 33;
                    break;
                case 6:
                    this.width = 27;
                    break;
                default:
                    throw new IllegalStateException("Too many siblings registered for " + this.leftSibling);
            }
            passOffset();
        }
    }

    private ExpLikeLayer setIndex(int indexIn) {
        this.index = indexIn;
        return this;
    }

    private int getSiblingAmount() {
        if (this.rightSibling == null)
            return 1;
        else
            return rightSibling.getSiblingAmount() + 1;
    }

    private void passOffset() {
        this.dxOffset = leftSibling.dxOffset + leftSibling.width + 4;
        if (rightSibling != null)
            this.rightSibling.passOffset();
    }

    protected float getProgress(Player player) {
        return fillProgress.apply(player);
    }

    protected void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player) {
        var value = getValue(player);
        var progress = getProgress(player);
        var xOffset = guiGraphics.guiWidth() / 2 + dxOffset;
        var yOffset = guiGraphics.guiHeight() - LayerWrapper.xpBarHeight + 3;
        guiGraphics.blitSprite(RenderType::guiTextured, this.background, xOffset, yOffset, width, 5);
        guiGraphics.blitSprite(RenderType::guiTextured, this.progress, width, 5, 0, 0, xOffset, yOffset, Math.round(width * progress), 5);
        renderText(guiGraphics, deltaTracker, player, xOffset, yOffset, value, progress);

        if (this.rightSibling != null)
            rightSibling.render(guiGraphics, deltaTracker, player);
        else
            LayerWrapper.xpBarHeight += 9;
    }

    protected void renderText(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value, float progress) {
        var text = getText(player, value, progress);
        var font = Minecraft.getInstance().font;
        var textXOffset = xOffset + width / 2 - font.width(text) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset, yOffset - 3, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset, yOffset - 1, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset - 1, yOffset - 2, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset + 1, yOffset - 2, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset, yOffset - 2, textColor, false);
    }

    protected String getText(Player player, float value, float progress) {
        return "" + value;
    }
}
