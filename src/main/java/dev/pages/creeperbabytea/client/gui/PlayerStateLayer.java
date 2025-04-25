package dev.pages.creeperbabytea.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class PlayerStateLayer implements LayeredDraw.Layer {
    protected final ResourceLocation id;
    protected final Function<Player, Float> value;

    protected Predicate<Minecraft> show;

    protected PlayerStateLayer(ResourceLocation id, Function<Player, Float> value) {
        this.id = id;
        this.value = value;
    }

    protected void onAdd2Wrapper(LayerWrapper wrapper) {
    }

    protected void setShowPredicate(Predicate<Minecraft> show) {
        this.show = show;
    }

    protected void appendShowPredicateAND(Predicate<Minecraft> append) {
        this.show = mc -> show.test(mc) && append.test(mc);
    }

    public float getValue(Player player) {
        return value.apply(player);
    }

    protected abstract void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Player player);

    @Override
    public final void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Profiler.get().push(this.id.toString());
        Player player = getCameraPlayer();
        if (player != null && show.test(Minecraft.getInstance()))
            this.render(guiGraphics, deltaTracker, player);
        Profiler.get().pop();
    }

    public void register(RegisterGuiLayersEvent event) {
        event.registerAboveAll(id, this);
    }

    public static Gui getGui() {
        return Minecraft.getInstance().gui;
    }

    @Nullable
    public static Player getCameraPlayer() {
        return Minecraft.getInstance().cameraEntity instanceof Player ret ? ret : null;
    }
}
