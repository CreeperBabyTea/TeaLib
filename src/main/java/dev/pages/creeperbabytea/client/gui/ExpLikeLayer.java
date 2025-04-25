package dev.pages.creeperbabytea.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ExpLikeLayer extends BarLikeLayer {
    /*public static final ResourceLocation EXP_LIKE_BACKGROUND = TeaLib.modLoc("hud/bar_exp_like_background");
    public static final ResourceLocation EXP_LIKE_PROGRESS = TeaLib.modLoc("hud/bar_exp_like_progress");
    public static final ResourceLocation MANA_LIKE_BACKGROUND = TeaLib.modLoc("hud/bar_mana_like_background");
    public static final ResourceLocation MANA_LIKE_PROGRESS = TeaLib.modLoc("hud/bar_mana_like_progress");*/
    protected final Function<Player, String> textToShow;

    private final int textColor;
    protected ExpLikeLayer leftSibling;
    protected ExpLikeLayer rightSibling;
    protected int siblingAmount;
    private int index = 0;
    protected int dxOffset;

    /**
     * 添加物品栏上方类似经验条的ui。原版的血量条、其他用TeaLib添加的ui会根据ui数量自动调整位置。
     * @param rightSibling 右姊妹。可以添加右姊妹实现一行渲染多个ui。通过该方法添加的一行ui只需要将最左边一个添加到{@link LayerWrapper}。
     */
    protected ExpLikeLayer(ResourceLocation id, Function<Player, Float> value, Function<Player, String> textToShow, int textColor, ResourceLocation backgroundSprite, ResourceLocation progressSprite, @Nullable ExpLikeLayer rightSibling) {
        super(id, value, backgroundSprite, progressSprite);
        this.height = 5;
        this.textColor = textColor;
        this.textToShow = textToShow;
        if (rightSibling != null) {
            this.rightSibling = rightSibling.setIndex(this.index + 1);
            this.rightSibling.leftSibling = this;
        }
        if (this.index == 0) {
            siblingAmount = getSiblingAmount();
            this.dxOffset = -91;
            this.width = (186 - siblingAmount * 4) / siblingAmount;
        } else {
            this.width = switch (siblingAmount) {
                case 2 -> 87;
                case 3 -> 58;
                case 4 -> (index == 1 || index == 2 ? 43 : 42);
                case 5 -> index == 2 ? 34 : 33;
                case 6 -> 27;
                default -> throw new IllegalStateException("Too many siblings registered for " + this.leftSibling);
            };
            passOffset();
        }
    }

    @Override
    protected void onAdd2Wrapper(LayerWrapper wrapper) {
        wrapper.addAboveHotbarLeftHeightDefault(9);
        wrapper.addAboveHotbarRightHeightDefault(9);
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

    protected void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player) {
        var value = getValue(player);
        var text = getText(player);
        var xOffset = guiGraphics.guiWidth() / 2 + dxOffset;
        var yOffset = guiGraphics.guiHeight() - LayerWrapper.getHotbarHeight() + 3;
        renderBarBackground(guiGraphics, deltaTracker, player, xOffset, yOffset, value);
        renderBarProgress(guiGraphics, deltaTracker, player, xOffset, yOffset, value);
        renderText(guiGraphics, deltaTracker, player, xOffset, yOffset, value, text);

        if (this.rightSibling != null)
            rightSibling.render(guiGraphics, deltaTracker, player);
        else
            LayerWrapper.addHotbarHeight(getAdditionHeight());
    }

    protected void renderText(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player, int xOffset, int yOffset, float value, String text) {
        var font = Minecraft.getInstance().font;
        var textXOffset = xOffset + width / 2 - font.width(text) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset, yOffset - 3, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset, yOffset - 1, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset - 1, yOffset - 2, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset + 1, yOffset - 2, 0, false);
        guiGraphics.drawString(Minecraft.getInstance().font, text, textXOffset, yOffset - 2, textColor, false);
    }

    @Override
    protected int getAdditionHeight() {
        return 9;
    }

    /*protected int getProgressColor(Player player, float value, float progress) {
        return 0xFFFFFFFF;
    }

    protected int getBackgroundColor(Player player, float value, float progress) {
        return 0xFFFFFFFF;
    }*/

    protected String getText(Player player) {
        return textToShow.apply(player);
    }
}
