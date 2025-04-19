package dev.pages.creeperbabytea.common.data;

import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.common.register.EntryInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public abstract class AdditionalInfoProvider<T> extends JsonCodecProvider<T> {
    public AdditionalInfoProvider(PackOutput output, PackOutput.Target target, String directory, Codec<T> codec, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, target, directory, codec, lookupProvider, modId);
    }
}
