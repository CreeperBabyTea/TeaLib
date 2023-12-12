package creeperbabytea.tealib.client.screen.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import creeperbabytea.tealib.client.screen.TScreen;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

public class TBook extends TScreen {
    private TBookCover.Front FRONT;
    private TBookCover.Back BACK;
    private int totalPage;
    private int currentPage;
    //private Supplier<TScreen>

    protected TBook(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //if (currentPage == 0)

    }
}
