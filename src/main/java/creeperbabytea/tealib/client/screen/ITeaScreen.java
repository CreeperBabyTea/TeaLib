package creeperbabytea.tealib.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IScreen;
import net.minecraft.client.gui.screen.Screen;

import javax.annotation.Nullable;

public interface ITeaScreen extends IScreen {
    @Nullable
    default GUIOpener open(ITeaScreen screen) {
        if (screen instanceof Screen)
            return () -> Minecraft.getInstance().displayGuiScreen((Screen) screen);
        else
            return null;
    }

    interface GUIOpener {
        void getScreen();
    }
}
