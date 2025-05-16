package dev.pages.creeperbabytea.common.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class CodecDataTranslator<T> {
    private final Codec<T> codec;
    private final ResourceLocation id;
    private final String directory;
    private final boolean read, write;

    private PackOutput.Target target = PackOutput.Target.DATA_PACK;

    public CodecDataTranslator(Codec<T> codec, ResourceLocation id, String directory, boolean read, boolean write) {
        this.codec = codec;
        this.id = id;
        this.directory = directory;
        this.read = read;
        this.write = write;
    }

    public CodecDataTranslator<T> target(PackOutput.Target target) {
        this.target = target;
        return this;
    }

    protected abstract void read(Map<ResourceLocation, T> object, ResourceManager resourceManager, ProfilerFiller profiler);

    protected abstract Map<ResourceLocation, T> write();

    public void init(IEventBus mod, IEventBus game) {
        if (read) {
            var reader = new Reader();
            game.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(id, reader));
            mod.addListener(AddClientReloadListenersEvent.class, event -> event.addListener(id, reader));
        }
        if (write)
            mod.addListener(GatherDataEvent.Client.class, event -> event.createProvider((Writer::new)));
    }

    public class Reader extends SimpleJsonResourceReloadListener<T> {
        protected Reader() {
            super(codec, FileToIdConverter.json(directory));
        }

        @Override
        protected void apply(Map<ResourceLocation, T> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            read(object, resourceManager, profiler);
        }
    }

    public class Writer extends JsonCodecProvider<T> {
        public Writer(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, target, CodecDataTranslator.this.directory, CodecDataTranslator.this.codec, lookupProvider, id.getNamespace());
        }

        @Override
        protected void gather() {
            write().forEach(this::unconditional);
        }
    }
}
