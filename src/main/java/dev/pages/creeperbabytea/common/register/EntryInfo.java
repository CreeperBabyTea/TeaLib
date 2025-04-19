package dev.pages.creeperbabytea.common.register;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * 包装的你的注册项。
 * @param <T> 注册项的最高父类，如{@link Item}
 * @param <I> 具体注册项的类
 */
public abstract class EntryInfo<T, I extends T> implements Supplier<T> {
    protected Supplier<I> entry;
    protected DeferredHolder<T, I> deferredHolder;

    protected CompoundTag info;

    public EntryInfo(Supplier<I> entry) {
        this(entry, new CompoundTag());
    }

    public EntryInfo(Supplier<I> entry, CompoundTag info) {
        this.entry = entry;
        this.setInfo(info);
    }

    public EntryInfo(DeferredHolder<T, I> holder, CompoundTag info) {
        this.setDeferredHolder(holder).setInfo(info);
    }

    protected EntryInfo<T, I> setEntry(Supplier<I> entry) {
        this.entry = entry;
        return this;
    }

    protected EntryInfo<T, I> setInfo(CompoundTag info) {
        this.info = info;
        return this;
    }

    EntryInfo<T, I> setDeferredHolder(DeferredHolder<T, I> deferredHolder) {
        this.deferredHolder = deferredHolder;
        this.setEntry(deferredHolder);
        return this;
    }

    @Nullable
    public DeferredHolder<T, I> getDeferredHolder() {
        return deferredHolder;
    }

    @Override
    public I get() {
        return entry.get();
    }

    public ResourceLocation getId() {
        return deferredHolder.getId();
    }

    public CompoundTag getInfo() {
        return info;
    }

    public abstract BuiltInEntryInfo<T> mapToRawInfo();

    /**
     * 仅用于解码原版或其他模组的物品，不提供注册、解包等功能。
     */
    public static final class BuiltInEntryInfo<T> extends EntryInfo<T, T> {
        private final Holder<T> holder;

        public BuiltInEntryInfo(Holder<T> holder, CompoundTag info) {
            super(holder::value, info);
            this.holder = holder;
        }

        public Holder<T> getHolder() {
            return holder;
        }

        @Override
        public T get() {
            return holder.value();
        }

        @Override
        public BuiltInEntryInfo<T> mapToRawInfo() {
            return this;
        }
    }
}
