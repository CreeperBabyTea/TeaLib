package dev.pages.creeperbabytea.common.register.info;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pages.creeperbabytea.common.data.CodecDataTranslator;
import dev.pages.creeperbabytea.util.GenericUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EntriesInfoProvider<T, I> {
    private final Type infoType = GenericUtils.getGenericParameter(this, 1);
    private final Map<T, I> infoMap = new HashMap<>(64);

    private final Codec<T> entryCodec;
    private final Codec<I> infoCodec;
    private final ResourceLocation id;
    private final String folderPath;
    private Codec<InfoBoundedWrapper<T, I>> codec;

    private final Parser parser;

    public EntriesInfoProvider(Codec<T> entryCodec, Codec<I> infoCodec, ResourceLocation id) {
        this(entryCodec, infoCodec, id, "entriesInfo/" + id.getNamespace() + '/' + id.getPath());
    }

    public EntriesInfoProvider(Codec<T> entryCodec, Codec<I> infoCodec, ResourceLocation id, String folderPath) {
        this.entryCodec = entryCodec;
        this.infoCodec = infoCodec;
        this.id = id;
        this.folderPath = folderPath;

        this.parser = new Parser();
    }

    public Codec<InfoBoundedWrapper<T, I>> getCodec() {
        return codec != null ? codec : (codec = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("entry").forGetter(w -> w.getId().getNamespace()),
                infoCodec.fieldOf("info").forGetter(InfoBoundedWrapper::getInfo)
        ).apply(instance, (string, info) -> {
            var entry = entryCodec.parse(JsonOps.INSTANCE, new JsonPrimitive(string)).getOrThrow();
            return new InfoBoundedWrapper<>(ResourceLocation.parse(string), entry, info);
        })));
    }

    public void put(T entry, I info) {
        this.infoMap.put(entry, info);
    }

    private void put(InfoBoundedWrapper<T, I> wrapper) {
        this.infoMap.put(wrapper.get(), wrapper.getInfo());
    }

    public boolean forcefullyContains(T entry) {
        return infoMap.containsKey(entry) || (entry instanceof IInfoProvider<?> && GenericUtils.getGenericParameter(entry, 0) == this.infoType);
    }

    public boolean weakContains(T entry) {
        return infoMap.containsKey(entry) || entry instanceof IInfoProvider<?>;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public I get(T entry) {
        if (entry instanceof IInfoProvider<?>)
            return ((IInfoProvider<I>) entry).get();
        return infoMap.get(entry);
    }

    public void init(IEventBus mod, IEventBus game) {
        parser.init(mod, game);
    }

    public class Parser extends CodecDataTranslator<InfoBoundedWrapper<T, I>> {
        public Parser() {
            super(codec, id, folderPath, true, false);
        }

        @Override
        protected void read(Map<ResourceLocation, InfoBoundedWrapper<T, I>> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            object.values().forEach(EntriesInfoProvider.this::put);
        }

        @Override
        protected Map<ResourceLocation, InfoBoundedWrapper<T, I>> write() {
            return Map.of();
        }
    }
}
