package creeperbabytea.tealib.client.screen.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sun.javafx.UnmodifiableArrayList;
import creeperbabytea.tealib.client.screen.TScreen;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class TBook extends TScreen {
    private final TBookCover.Front FRONT;
    private final TBookCover.Back BACK;
    private final List<TBookElement> ELEMENTS;

    private int totalContent;
    private int contentIndex;

    private int totalPage;
    private int currentPage;

    protected TBook(ITextComponent titleIn, TBookCover.Front front, TBookCover.Back back, TBookElement... elements) {
        super(titleIn);
        FRONT = front;
        BACK = back;
        ELEMENTS = new UnmodifiableArrayList<>(elements, elements.length);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //if (currentPage == 0)

    }

    public static Builder builder(ITextComponent title) {
        return new Builder(title);
    }

    public static class Builder {
        private final ITextComponent title;
        private TBookCover front;
        private TBookCover back;
        private ArrayList<TBookElement> elements;

        private Builder(ITextComponent title) {
            this.title = title;
        }

        public Builder front(TBookCover front) {
            this.front = front;
            return this;
        }

        public Builder back(TBookCover back) {
            this.back = back;
            return this;
        }

        /*public TBook build() {
            //return new TBook(title, front, back, elements);
        }*/
    }
}
