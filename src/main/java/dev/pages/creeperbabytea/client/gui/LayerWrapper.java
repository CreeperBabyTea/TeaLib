package dev.pages.creeperbabytea.client.gui;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerWrapper {
    private static final List<ExpLikeLayer> EXP_LIKE = new ArrayList<>();
    private static final List<HealthLikeLayer> HEALTH_LIKE = new ArrayList<>();
    //+8 per
    public static int xpBarHeight = 41;
    //+10 per health +8 per xp
    public static int leftHeight = 41;
    public static int rightHeight = 41;

    public static <L extends ExpLikeLayer> L registerExpLike(L layer) {
        EXP_LIKE.add(layer);
        LayerWrapper.leftHeight += 9;
        LayerWrapper.rightHeight += 9;
        return layer;
    }

    public static <L extends HealthLikeLayer> L registerHealthLike(L layer) {
        HEALTH_LIKE.add(layer);
        return layer;
    }

    @SubscribeEvent
    public static void onRenderingGui(RegisterGuiLayersEvent event) {
        EXP_LIKE.forEach(l -> l.register(event));
        HEALTH_LIKE.forEach(l -> l.register(event));
    }

    public static void reload() {
        xpBarHeight = 41;
        leftHeight = 41 + 9 * EXP_LIKE.size();
        rightHeight = 41 + 9 * EXP_LIKE.size();
    }
}
