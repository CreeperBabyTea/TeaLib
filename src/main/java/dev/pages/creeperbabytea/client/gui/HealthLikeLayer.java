package dev.pages.creeperbabytea.client.gui;

import dev.pages.creeperbabytea.TeaLib;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.function.Function;

public class HealthLikeLayer extends PlayerStateLayer {
    private final boolean isOnLeftSide;
    private final boolean renderOnZeroValue;
    private final int valPerIcon;
    private final int dxOffset;
    private final ResourceLocation[] sprites;


    /**
     * @param sprites 渲染所需的图像。图像个数应该等于valPerIcon+1，其中第一个是空的，最后一个是满的
     */
    protected HealthLikeLayer(ResourceLocation id, Function<Player, Float> value, int valPerIcon, boolean isOnLeftSide, boolean renderOnZeroValue, ResourceLocation... sprites) {
        super(id, value);
        this.isOnLeftSide = isOnLeftSide;
        this.renderOnZeroValue = renderOnZeroValue;
        this.valPerIcon = valPerIcon;
        this.dxOffset = isOnLeftSide ? -91 : 11;
        if (sprites.length > valPerIcon + 1) {
            this.sprites = Arrays.copyOf(sprites, valPerIcon + 1);
            TeaLib.LOGGER.warn("Too many sprites found for {}! Check value per icon of the constructor.", this);
        } else if (sprites.length == valPerIcon + 1)
            this.sprites = sprites;
        else
            throw new IllegalStateException("Not enough sprites for rendering " + this);
    }

    public ResourceLocation[] getSprites() {
        return sprites;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player) {
        int xOffset = guiGraphics.guiWidth() / 2 + dxOffset;
        float value = getValue(player);
        final int valPerLine = valPerIcon * 10;
        final ResourceLocation[] sprites = getSprites();
        int lines = (int) (Math.ceil(value) / valPerLine);
        int lineHeight = Math.max(10 - (lines - 2), 3);
        while (value >= 10) {
            value /= 10;

            for (int i = 0; i < 10; i++)
                guiGraphics.blitSprite(RenderType::guiTextured, sprites[valPerIcon + 1], xOffset + 8 * i, getYOffset(guiGraphics), 9, 9);

            if (this.isOnLeftSide)
                getGui().leftHeight += lineHeight;
            if (!this.isOnLeftSide)
                getGui().rightHeight += lineHeight;
        }
    }

    private int getYOffset(GuiGraphics guiGraphics) {
        return guiGraphics.guiHeight() - (isOnLeftSide ? getGui().leftHeight : getGui().rightHeight);
    }
}
