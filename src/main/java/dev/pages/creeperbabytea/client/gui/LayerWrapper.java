package dev.pages.creeperbabytea.client.gui;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例。INSTANCE仅在添加层的时候提供给层。
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerWrapper {
    private static final LayerWrapper INSTANCE = new LayerWrapper();

    private static final List<PlayerStateLayer> LAYERS = new ArrayList<>();

    private LayerWrapper() {}

    /**
     * 物品栏上的所有ui的高度
     */
    private int hotbarHeightDefault = 41;//+9 per xp like layer

    /**
     * 物品栏上方左侧高度，即血量那一栏的高度。
     */
    private int aboveHotbarLeftDefault = 41;//+10 per health like layer +9 per xp like layer
    /**
     * 物品栏上方右侧高度，即干饭那一栏的高度。
     */
    private int aboveHotbarRightDefault = 41;
    /**
     * 物品栏旁边左侧高度
     */
    private int besideHotbarLeftDefault = 0;
    /**
     * 物品栏旁边右侧高度
     */
    private int besideHotbarRightDefault = 0;

    public int hotbarHeight;
    public int aboveHotbarLeft;
    public int aboveHotbarRight;
    public int besideHotbarLeft;
    public int besideHotbarRight;

    public static <L extends PlayerStateLayer> L register(L layer) {
        LAYERS.add(layer);
        layer.onAdd2Wrapper(INSTANCE);
        return layer;
    }

    @SubscribeEvent
    public static void onRenderingGui(RegisterGuiLayersEvent event) {
        LAYERS.forEach(l -> l.register(event));
    }

    public static void reload() {
        INSTANCE.hotbarHeight = INSTANCE.hotbarHeightDefault;
        INSTANCE.aboveHotbarLeft = INSTANCE.aboveHotbarLeftDefault;
        INSTANCE.aboveHotbarRight = INSTANCE.aboveHotbarRightDefault;
        INSTANCE.besideHotbarLeft = INSTANCE.besideHotbarLeftDefault;
        INSTANCE.besideHotbarRight = INSTANCE.besideHotbarRightDefault;
    }

    public static int getHotbarHeightDefault() {
        return INSTANCE.hotbarHeightDefault;
    }

    public void addHotbarHeightDefault(int hotbarHeightDefault) {
        this.hotbarHeightDefault += hotbarHeightDefault;
    }

    public static int getAboveHotbarLeftHeightDefault() {
        return INSTANCE.aboveHotbarLeftDefault;
    }

    public void addAboveHotbarLeftHeightDefault(int aboveHotbarLeftHeightDefault) {
        this.aboveHotbarLeftDefault += aboveHotbarLeftHeightDefault;
    }

    public static int getAboveHotbarRightHeightDefault() {
        return INSTANCE.aboveHotbarRightDefault;
    }

    public void addAboveHotbarRightHeightDefault(int aboveHotbarRightHeightDefault) {
        this.aboveHotbarRightDefault += aboveHotbarRightHeightDefault;
    }

    public static int getHotbarHeight() {
        return INSTANCE.hotbarHeight;
    }

    public static void addHotbarHeight(int pixels) {
        INSTANCE.hotbarHeight += pixels;
    }

    public static int getAboveHotbarLeft() {
        return INSTANCE.aboveHotbarLeft;
    }

    public static void addAboveHotbarLeft(int pixels) {
        INSTANCE.aboveHotbarLeft += pixels;
    }

    public static int getAboveHotbarRight() {
        return INSTANCE.aboveHotbarRight;
    }

    public static void addAboveHotbarRight(int pixels) {
        INSTANCE.aboveHotbarRight += pixels;
    }

    public static int getBesideHotbarLeft() {
        return INSTANCE.besideHotbarLeft;
    }

    public static void addBesideHotbarLeft(int pixels) {
        INSTANCE.besideHotbarLeft += pixels;
    }

    public static int getBesideHotbarRight() {
        return INSTANCE.besideHotbarRight;
    }

    public static void addBesideHotbarRight(int pixels) {
        INSTANCE.besideHotbarRight += pixels;
    }
}
