package dev.pages.creeperbabytea.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeakRegistry<T> {
    private final String namespace;
    private final Map<String, WeakHolder<T>> byId = new LinkedHashMap<>();
    private boolean locked = false;

    public WeakRegistry(String namespace) {
        this.namespace = namespace;
    }

    public <I extends T> WeakHolder<T> registerSingleton(String id, I entry) {
        if (locked)
            throw new IllegalStateException("Can't add weak registry to one that's already locked!");
        if (byId.containsKey(id))
            throw new IllegalStateException("Duplicated weak registry: " + id);

        var key = ResourceLocation.fromNamespaceAndPath(namespace, id);
        var ret = new WeakHolder<T>(key, entry);
        byId.put(id, ret);
        return ret;
    }

    public void lock() {
        this.locked = true;
    }

    @Nullable
    public WeakHolder<T> getByName(String id) {
        return byId.get(id);
    }

    @Nullable
    public WeakHolder<T> getById(ResourceLocation loc) {
        return loc.getNamespace().equals(this.namespace) ? byId.get(loc.getPath()) : null;
    }
}
