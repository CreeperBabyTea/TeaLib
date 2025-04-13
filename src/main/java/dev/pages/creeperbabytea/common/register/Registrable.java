package dev.pages.creeperbabytea.common.register;

import net.minecraft.resources.ResourceLocation;

public interface Registrable<T extends Registrable<T>> {
    void setName(ResourceLocation name);

    ResourceLocation getName();
}
