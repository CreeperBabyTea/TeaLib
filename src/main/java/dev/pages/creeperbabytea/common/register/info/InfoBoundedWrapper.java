package dev.pages.creeperbabytea.common.register.info;

import net.minecraft.resources.ResourceLocation;

/**
 * 仅用于读写原版物品的额外数据。<br>
 * 对于你自己注册的内容，你完全可以在自己的类里面添加字段保存，
 */
public class InfoBoundedWrapper<T, I> extends EntryWrapper<T, T> {
    private final I info;

    public InfoBoundedWrapper(ResourceLocation id, T entry, I info) {
        super(id, () -> entry);
        this.info = info;
    }

    public I getInfo() {
        return info;
    }
}
