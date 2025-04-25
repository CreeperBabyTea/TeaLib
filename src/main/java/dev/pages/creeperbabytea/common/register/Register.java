package dev.pages.creeperbabytea.common.register;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.TeaLib;
import dev.pages.creeperbabytea.common.data.AdditionalInfoProvider;
import dev.pages.creeperbabytea.mixin.DeferredRegisterAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Register<T> extends DeferredRegister<T> {
    private static final Collection<ResourceLocation> REGISTERS_WITH_ADDITIONAL_INFO = new HashSet<>();
    private static final Map<ResourceLocation, Collection<Register<?>>> REGISTERS = new LinkedHashMap<>();

    private final ResourceLocation registry;
    private EntryInfoProvider<T> infoProvider;
    protected Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entriesHooked;

    public <I extends Registry<T>> Register(ResourceKey<I> registryKey, String modid) {
        super(registryKey, modid);

        this.registry = registryKey.registry();
        this.entriesHooked = ((DeferredRegisterAccessor<T>) this).getEntries();

        if (!REGISTERS.containsKey(registry))
            REGISTERS.put(registry, Sets.newLinkedHashSet());
        REGISTERS.get(registry).add(this);

        if (REGISTERS_WITH_ADDITIONAL_INFO.contains(this.registry))
            this.infoProvider = (EntryInfoProvider<T>) REGISTERS.values().stream().toList().getLast().stream().toList().getLast().infoProvider;
    }

    public Register(ResourceLocation registry, String namespace) {
        this(ResourceKey.createRegistryKey(registry), namespace);
    }

    public Register(Registry<T> registry, String namespace) {
        this(registry.key(), namespace);
    }

    public Register<T> registerAdditionalInfo(Codec<Holder<T>> entryCodec) {
        this.infoProvider = new EntryInfoProvider<>(entryCodec);
        REGISTERS_WITH_ADDITIONAL_INFO.add(this.registry);
        if (!REGISTERS.containsKey(registry)) {
            REGISTERS.put(registry, new LinkedHashSet<>());
            for (Register<?> Register : REGISTERS.get(registry)) {
                ((Register<T>) Register).infoProvider = this.infoProvider; //不转换你报错，转换了你又说冗余...唐完了
            }
        }
        return this;
    }

    /**
     * 仅用于为{@link AdditionalInfoProvider}添加额外物品信息
     */
    public Register<T> putInfo(EntryInfo<T, ? extends T> state) {
        if (noAdditionalInfo()) {
            TeaLib.LOGGER.error("Can't put state because no state for this entry type is defined: {}", this.registry);
            return this;
        }
        this.infoProvider.put(state);
        return this;
    }

    public boolean hasInfoFor(T entry) {
        if (noAdditionalInfo()) {
            return false;
        } else return this.infoProvider.containsKey(entry);
    }

    @Nullable
    public <I extends T> EntryInfo<T, I> getInfo(I entry) {
        if (noAdditionalInfo()) {
            TeaLib.LOGGER.error("Can't get state because no state for this entry type is defined: {}", this.registry);
            return null;
        }

        return infoProvider.get(entry);
    }

    public void forEachEntryInfo(BiConsumer<T, EntryInfo<T, ? extends T>> consumer) {
        if (noAdditionalInfo()) {
            TeaLib.LOGGER.error("Can't run for each state because no state for this entry type is defined: {}", this.registry);
            return;
        }
        this.infoProvider.forEach(consumer);
    }

    public boolean noAdditionalInfo() {
        return this.infoProvider == null;
    }

    public <I extends T> EntryInfo<T, I> register(String name, EntryInfo<T, I> info) {
        if (this.noAdditionalInfo())
            this.infoProvider.put(info);
        return info.setDeferredHolder(register(name, info::get));
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

    public Codec<EntryInfo.BuiltInEntryInfo<T>> getInfoCodec() {
        return infoProvider.codec;
    }

    /**
     * 如果你要注册额外的事件监听器，或者实现额外的注册行为，重写这个方法。
     */
    protected void customEventRegister(IEventBus mod, IEventBus forge) {
        entriesHooked.values().forEach(v -> {
            if (v instanceof Registrable<?> r && r.hasCustomListeners())
                r.registerCustomListeners(mod, forge);
        });
    }

    public static void clearUp() {
        REGISTERS.clear();
        REGISTERS_WITH_ADDITIONAL_INFO.clear();
    }
}
