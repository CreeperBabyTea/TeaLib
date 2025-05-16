package dev.pages.creeperbabytea.common.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class WeakHolder<T> implements Supplier<T> {
    private final T entry;
    private final ResourceLocation id;

    public WeakHolder( ResourceLocation id, T entry) {
        this.entry = entry;
        this.id = id;
    }

    @Override
    public T get() {
        return entry;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
