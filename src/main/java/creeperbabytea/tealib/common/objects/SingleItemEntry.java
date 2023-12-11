package creeperbabytea.tealib.common.objects;

import creeperbabytea.tealib.util.IItemProviderAndSupplier;
import net.minecraft.item.Item;

public class SingleItemEntry extends SingleEntry<Item> implements IItemProviderAndSupplier {
    public SingleItemEntry(Item entry, String name, String group) {
        super(entry, name, group);
    }

    public SingleItemEntry(Item.Properties properties, String name, String group) {
        this(new Item(properties), name, group);
    }

    public SingleItemEntry(Item.Properties properties, String name) {
        this(new Item(properties), name, "");
    }

    @Override
    public Class<Item> getType() {
        return Item.class;
    }
}
