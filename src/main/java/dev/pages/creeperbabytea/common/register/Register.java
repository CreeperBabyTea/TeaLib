package dev.pages.creeperbabytea.common.register;

import dev.pages.creeperbabytea.TeaLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Register<T> extends DeferredRegister<T> {
    private static final Map<ResourceLocation, Map> STATE_MAPS = new HashMap<>();
    private static final Map<ResourceLocation, Collection<Register<?>>> REGISTERS = new LinkedHashMap<>();

    private final ResourceLocation registry;
    private Map stateMap;

    public <I extends Registry<T>> Register(ResourceKey<I> registryKey, String modid) {
        super(registryKey, modid);

        this.registry = registryKey.registry();
        if (STATE_MAPS.containsKey(registry))
            this.stateMap = STATE_MAPS.get(registry);

        if (!REGISTERS.containsKey(registry))
            REGISTERS.put(registry, new LinkedHashSet<>());
        REGISTERS.get(registry).add(this);
    }

    public Register(ResourceLocation registry, String namespace) {
        this(ResourceKey.createRegistryKey(registry), namespace);
    }

    public Register(Registry<T> registry, String namespace) {
        this(registry.key(), namespace);
    }

    public Register<T> makeStateMap() {
        if (!STATE_MAPS.containsKey(registry)) {
            this.stateMap = new LinkedHashMap<>();
            STATE_MAPS.put(registry, this.stateMap);
        }
        if (!REGISTERS.containsKey(registry)) {
            REGISTERS.put(registry, new LinkedHashSet<>());
            for (Register<?> register : REGISTERS.get(registry)) {
                register.stateMap = this.stateMap;
            }
        }
        return this;
    }

    public Register<T> putState(T entry, EntryState<T> state) {
        if (reqState()) {
            TeaLib.LOGGER.error("Can't put state because no state for this entry type is defined: {}", this.registry);
            return this;
        }

        this.stateMap.put(entry, state);
        return this;
    }

    public boolean hasState(T entry) {
        if (reqState()) {
            return false;
        } else return this.stateMap.containsKey(entry);
    }

    @Nullable
    public EntryState<T> getState(T entry) {
        if (reqState()) {
            TeaLib.LOGGER.error("Can't get state because no state for this entry type is defined: {}", this.registry);
            return null;
        }

        return (EntryState<T>) stateMap.get(entry);
    }

    public boolean reqState() {
        return !(this.stateMap == null);
    }

    @Override
    public <I extends T> DeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
        DeferredHolder<T, I> ret = super.register(name, func);
        if (func.apply(ret.getId()) instanceof Registrable)
            ((Registrable<?>) func.apply(ret.getId())).setName(ResourceLocation.fromNamespaceAndPath(this.getNamespace(), name));
        return ret;
    }

    public final void register(IEventBus mod, IEventBus game) {
        customEventRegister(mod, game);
        super.register(mod);
    }

    @Override
    @Deprecated
    public final void register(IEventBus bus) {
        throw new UnsupportedOperationException("Please use the register(IEventBus, IEventBus) method instead.");
    }

    /**
     * When you need some custom event behaviors you can override this method.
     */
    protected void customEventRegister(IEventBus mod, IEventBus forge) {
    }

    public static void clearUp() {
        REGISTERS.clear();
    }
}
