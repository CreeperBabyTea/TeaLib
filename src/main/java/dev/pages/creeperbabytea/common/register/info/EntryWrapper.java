package dev.pages.creeperbabytea.common.register.info;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 包装的你的注册项。
 * @param <T> 注册项的最高父类，如{@link Item}
 * @param <TT> 具体注册项的类
 */
public class EntryWrapper<T, TT extends T> implements Supplier<TT> {
    protected ResourceLocation id;
    protected final Supplier<TT> entrySup;

    protected Holder<T> holder;

    public EntryWrapper(ResourceLocation id, Supplier<TT> entrySup) {
        this.id = id;
        this.entrySup = entrySup;
    }

    public void bindHolder(Registry<T> registry) {
        this.holder = registry.get(id).orElse(null);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Supplier<TT> getEntrySup() {
        return entrySup;
    }

    @Nullable
    public TT getEntryNullable() {
        return entrySup.get();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EntryWrapper<?, ?> that))
            return false;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public TT get() {
        return Objects.requireNonNull(this.entrySup.get());
    }
}
