package dev.pages.creeperbabytea.common.register;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.common.data.AdditionalInfoProvider;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class EntryInfoProvider<T> {
    private final Map<T, EntryInfo<T, ? extends T>> infoMap = new LinkedHashMap<>();
    protected final Codec<EntryInfo.BuiltInEntryInfo<T>> codec;

    private final Codec<Holder<T>> entryCodec;

    public EntryInfoProvider(Codec<Holder<T>> entryCodec) {
        this.entryCodec = entryCodec;

        this.codec = RecordCodecBuilder.create(instance -> instance.group(
                entryCodec.fieldOf("entry").forGetter(EntryInfo.BuiltInEntryInfo::getHolder),
                CompoundTag.CODEC.fieldOf("info").forGetter(EntryInfo.BuiltInEntryInfo::getInfo)
        ).apply(instance, EntryInfo.BuiltInEntryInfo::new));
    }

    public <I extends T> EntryInfo<T, I> get(I entry) {
        return (EntryInfo<T, I>) infoMap.get(entry);
    }

    public <I extends T> EntryInfo<T, I> put(EntryInfo<T, I> info) {
        return (EntryInfo<T, I>) infoMap.put(info.get(), info);
    }

    public boolean containsKey(T entry) {
        return infoMap.containsKey(entry);
    }

    public void forEach(BiConsumer<T, EntryInfo<T, ? extends T>> consumer) {
        infoMap.forEach(consumer);
    }
}
