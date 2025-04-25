package dev.pages.creeperbabytea.common.register;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

/**
 * 我讨厌你
 */
public interface Registrable<T extends Registrable<T>> {
    void setName(ResourceLocation name);

    ResourceLocation getName();

    default boolean hasCustomListeners() {
        return false;
    }

    default void registerCustomListeners(IEventBus mod, IEventBus game) {
    }
}
