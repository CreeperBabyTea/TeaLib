package creeperbabytea.tealib.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public abstract class TScreen extends Screen {
    protected TScreen(ITextComponent titleIn) {
        super(titleIn);
    }
}
