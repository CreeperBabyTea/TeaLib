package dev.pages.creeperbabytea.common.item.component;

import dev.pages.creeperbabytea.mixin.CustomDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;

public class CustomDataPatcher {
    public static CustomDataAccessor patch(CustomData data) {
        return (CustomDataAccessor) (Object) data;
    }

    public static boolean contains(CustomData data, String name) {
        return patch(data).getTag().contains(name);
    }

    public static Tag getChild(CustomData data, String name) {
        return patch(data).getTag().get(name);
    }

    public static Tag appendChild(CustomData data, String name, Tag tag) {
        return patch(data).getTag().put(name, tag);
    }
}
