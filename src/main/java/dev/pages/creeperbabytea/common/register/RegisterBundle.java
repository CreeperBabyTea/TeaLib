package dev.pages.creeperbabytea.common.register;

import dev.pages.creeperbabytea.TeaLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class RegisterBundle {
    private final String namespace;
    private final Map<ResourceLocation, Register<?>> registers = new LinkedHashMap<>();

    public RegisterBundle(String namespace) {
        this.namespace = namespace;
    }

    public <T, R extends Register<T>> R create(ResourceLocation type, R register) {
        this.registers.put(type, register);
        return register;
    }

    public <T, R extends Register<T>> R create(ResourceLocation type, BiFunction<ResourceLocation, String, R> registerMaker) {
        R register = registerMaker.apply(type, namespace);
        this.registers.put(type, register);
        return register;
    }

    @SuppressWarnings("unchecked")
    public <T> Register<T> getOrCreate(ResourceLocation type) {
        if (!registers.containsKey(type))
            return create(type, new Register<>(type, namespace));
        return (Register<T>) registers.get(type);
    }

    @SuppressWarnings("unchecked")
    public <T> Register<T> getOrCreate(Registry<T> type) {
        if (!registers.containsKey(type.key().registry()))
            return create(type.key().location(), new Register<>(type, namespace));
        return (Register<T>) registers.get(type.key().registry());
    }

    @SuppressWarnings("unchecked")
    public <I extends Registry<T>, T> Register<T> getOrCreate(ResourceKey<I> type) {
        if (!registers.containsKey(type.registry()))
            return create(type.location(), new Register<>(type, namespace));
        return (Register<T>) registers.get(type.registry());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> Register<T> get(ResourceKey<T> type) {
        try {
            return (Register<T>) registers.get(type.registry());
        } catch (ClassCastException e) {
            TeaLib.LOGGER.error("Wrong type of Register found in Registration {}", this);
            return null;
        }
    }

    public void registerAll(IEventBus mod, IEventBus forge) {
        this.registers.values().forEach(r -> r.register(mod, forge));
    }

    public String getNamespace() {
        return namespace;
    }
}
