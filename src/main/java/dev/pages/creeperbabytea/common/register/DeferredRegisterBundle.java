package dev.pages.creeperbabytea.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class DeferredRegisterBundle {
    private final String namespace;
    private final Map<ResourceLocation, DeferredRegister<?>> registerMap = new HashMap<>();

    public DeferredRegisterBundle(String namespace) {
        this.namespace = namespace;
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getOrCreate(ResourceLocation location) {
        if (registerMap.containsKey(location))
            return (DeferredRegister<T>) registerMap.get(location);
        var ret = DeferredRegister.<T>create(location, namespace);
        registerMap.put(location, ret);
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getOrCreate(ResourceKey<? extends Registry<T>> key) {
        if (registerMap.containsKey(key.location()))
            return (DeferredRegister<T>) registerMap.get(key.location());
        var ret = DeferredRegister.create(key, namespace);
        registerMap.put(key.location(), ret);
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getOrCreate(Registry<T> registry) {
        if (registerMap.containsKey(registry.key().location()))
            return (DeferredRegister<T>) registerMap.get(registry.key().location());
        var ret = DeferredRegister.create(registry, namespace);
        registerMap.put(registry.key().location(), ret);
        return ret;
    }

    public void register(IEventBus mod) {
        this.registerMap.values().forEach(reg -> reg.register(mod));
    }
}
