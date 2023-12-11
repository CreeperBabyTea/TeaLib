package creeperbabytea.tealib.util;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

import java.util.function.Supplier;

public interface IItemProviderAndSupplier extends IItemProvider, Supplier<Item> {
    @Override
    default Item asItem() {
        return this.get();
    }
}
