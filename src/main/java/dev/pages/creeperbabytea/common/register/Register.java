package dev.pages.creeperbabytea.common.register;

import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.mixin.DeferredRegisterAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Register<T> extends DeferredRegister<T> {
    private final ResourceLocation registryLocation;
    protected Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entriesHooked;

    public <I extends Registry<T>> Register(ResourceKey<I> registryKey, String modid) {
        super(registryKey, modid);

        this.registryLocation = registryKey.registry();
        this.entriesHooked = ((DeferredRegisterAccessor<T>) this).getEntries();
    }

    public Codec<T> byNameCodec() {
        return getRegistry().get().byNameCodec();
    }

    public Codec<Holder<T>> holderByNameCodec() {
        return getRegistry().get().holderByNameCodec();
    }

    public Register(ResourceLocation registryLocation, String namespace) {
        this(ResourceKey.createRegistryKey(registryLocation), namespace);
    }

    public Register(Registry<T> registryLocation, String namespace) {
        this(registryLocation.key(), namespace);
    }

    public <I extends T> DeferredHolder<T, I> registerSingleton(String name, I  sup) {
        if (sup instanceof RegistrableSingleton registrable)
            registrable.setName(ResourceLocation.fromNamespaceAndPath(getNamespace(), name));
        return super.register(name, () -> sup);
    }

    public final void register(IEventBus mod, IEventBus game) {
        customEventRegister(mod, game);
        super.register(mod);
    }

    public Register<T> emptyRegistry() {
        makeRegistry(whyAmIDoingThis -> {});
        return this;
    }

    @Override
    @Deprecated
    public final void register(IEventBus bus) {
        throw new UnsupportedOperationException("Please use the register(IEventBus, IEventBus) method instead.");
    }

    /**
     * 如果你要注册额外的事件监听器，或者实现额外的注册行为，重写这个方法。
     */
    protected void customEventRegister(IEventBus mod, IEventBus forge) {
        entriesHooked.values().forEach(v -> {
            if (v.get() instanceof RegistrableSingleton r && r.hasCustomListeners())
                r.registerCustomListeners(mod, forge);
        });
    }

    public ResourceLocation getRegistryLocation() {
        return registryLocation;
    }
}
