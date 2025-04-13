package dev.pages.creeperbabytea.common.register;


import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EntryState<E> implements Supplier<E> {
    private final E entry;

    private DeferredHolder<E, ? extends E> registryObject;

    public EntryState(E entry) {
        this.entry = entry;
    }

    public E getEntry() {
        return entry;
    }

    public DeferredHolder<E, ? extends E> register(String name, Register<E> register) {
        return registryObject = register.register(name, this);
    }

    @Nullable
    public DeferredHolder<E, ? extends E> getHolder() {
        return registryObject;
    }

    @Override
    public E get() {
        return entry;
    }
}
