package creeperbabytea.tealib.client.screen.book;

import creeperbabytea.tealib.client.screen.TScreen;
import net.minecraft.util.text.ITextComponent;

public abstract class TBookElement extends TScreen {
    protected TBookElement(ITextComponent titleIn) {
        super(titleIn);
    }
}
