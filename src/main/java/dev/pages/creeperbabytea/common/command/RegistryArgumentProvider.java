package dev.pages.creeperbabytea.common.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RegistryArgumentProvider<T> {
    private final RegistryArgumentType instance = new RegistryArgumentType();
    private final Supplier<ResourceKey<? extends Registry<T>>> resourceKey;
    private final Class<T> tClass;

    private DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<RegistryArgumentType, SingletonArgumentInfo<RegistryArgumentType>.Template>> entryWrapper;

    public RegistryArgumentProvider(Supplier<ResourceKey<? extends Registry<T>>> resourceKey, Class<T> clazz) {
        this.resourceKey = resourceKey;
        this.tClass = clazz;
    }

    public T getEntry(CommandContext<CommandSourceStack> ctx, String name) {
        return ctx.getArgument(name, tClass);
    }

    public RegistryArgumentType registry(CommandBuildContext context) {
        return new RegistryArgumentType(context);
    }

    public ArgumentTypeInfo<RegistryArgumentType, SingletonArgumentInfo<RegistryArgumentType>.Template> getInfo() {
        return SingletonArgumentInfo.contextAware(this::registry);
    }

    public RegistryArgumentProvider<T> register(IEventBus game, DeferredRegister<ArgumentTypeInfo<?, ?>> register) {
        this.entryWrapper = register.register(tClass.getSimpleName().toLowerCase(), this::getInfo);
        game.addListener(RegisterCommandsEvent.class, event -> ArgumentTypeInfos.registerByClass(getArgClass(), entryWrapper.get()));
        return this;
    }

    @SuppressWarnings("unchecked")
    private Class<RegistryArgumentProvider<T>.RegistryArgumentType> getArgClass() {
       return (Class<RegistryArgumentType>) instance.getClass();
    }

    public class RegistryArgumentType implements ArgumentType<T> {
        private HolderLookup.RegistryLookup<T> lookup;

        private RegistryArgumentType() {}

        private RegistryArgumentType(CommandBuildContext context) {
            this.lookup = context.lookupOrThrow(resourceKey.get());
        }

        @Nullable
        @Override
        public T parse(StringReader reader) throws CommandSyntaxException {
            var loc = ResourceLocation.read(reader);
            var ref = lookup.get(ResourceKey.create(resourceKey.get(), loc));
            return ref.map(Holder::value).orElse(null);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggestResource(lookup.listElementIds().map(ResourceKey::location), builder);
        }
    }
}
