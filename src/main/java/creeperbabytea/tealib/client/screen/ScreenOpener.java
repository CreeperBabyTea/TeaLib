package creeperbabytea.tealib.client.screen;

import net.minecraftforge.fml.DistExecutor;

public class ScreenOpener {
    public static DistExecutor.SafeCallable<?> open(ITeaScreen screen) {
        return () -> screen.open(screen);
    }
}
