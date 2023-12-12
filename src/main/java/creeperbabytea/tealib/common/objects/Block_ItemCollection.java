package creeperbabytea.tealib.common.objects;

import creeperbabytea.tealib.registry.GeneralRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Block_ItemCollection extends AbstractCollectionEntry<Block_ItemCollection> {
    private final Map<String, CollectionItemEntry> ITEMS = new HashMap<>();
    private final Map<String, CollectionBlockCollection> BLOCKS = new HashMap<>();

    public Block_ItemCollection(String name, String group) {
        super(name, group);
    }

    @Override
    public Block_ItemCollection register(GeneralRegister register) {
        this.ITEMS.forEach((type, entry) -> register.add(entry.getName() + '_' + type, entry.get(), entry.getType()));
        this.BLOCKS.forEach((type, entry) -> {
            register.add(entry.getName() + '_' + type, entry.getBlock().get(), entry.getBlock().getType());
            register.add(entry.getName() + '_' + type, entry.getItem().get(), entry.getItem().getType());
        });
        return this;
    }

    @Override
    public Block_ItemCollection put(String type, AbstractRegistrableEntry<?> value) {
        if (value instanceof CollectionItemEntry) {
            CollectionItemEntry entry = (CollectionItemEntry) value;
            if (entry.getParent() != null)
                entry.parent(this);
            this.ITEMS.put(type, entry);
        }
        if (value instanceof CollectionBlockCollection) {
            CollectionBlockCollection entry = (CollectionBlockCollection) value;
            if (entry.getParent() != null)
                entry.parent(this);
            this.BLOCKS.put(type, ((CollectionBlockCollection) value).parent(this));
        }
        return this;
    }

    @Nullable
    @Override
    public AbstractRegistrableEntry<?> get(String type) {
        if (ITEMS.containsKey(type))
            return ITEMS.get(type);
        if (BLOCKS.containsKey(type))
            return BLOCKS.get(type);
        return null;
    }

    public static class CollectionItemEntry extends SingleItemEntry {
        private Block_ItemCollection parent;

        public CollectionItemEntry(Item entry, String name, String group) {
            super(entry, name, group);
        }

        public CollectionItemEntry(Item.Properties properties, String name, String group) {
            super(properties, name, group);
        }

        public CollectionItemEntry(Item.Properties properties, String name) {
            super(properties, name);
        }

        @Override
        public SingleEntry<Item> register(GeneralRegister register) {
            throw new IllegalStateException("Collection items shall be registered within a collection!");
        }

        public CollectionItemEntry parent(Block_ItemCollection parent) {
            if (this.parent != null)
                throw new IllegalStateException("Attempted to set parent with existing parent! New: " + parent + " Old: " + this.parent);
            this.parent = parent;
            return this;
        }

        @Nullable
        public Block_ItemCollection getParent() {
            return this.parent;
        }
    }

    public static class CollectionBlockCollection extends BlockCollection {
        private Block_ItemCollection parent;

        public CollectionBlockCollection(Block block, Item.Properties blockItem, String name, String group) {
            super(block, blockItem, name, group);
        }

        public CollectionBlockCollection(Block block, ItemGroup tab, String name, String group) {
            super(block, tab, name, group);
        }

        public CollectionBlockCollection(AbstractBlock.Properties block, ItemGroup tab, String name) {
            super(block, tab, name);
        }

        public CollectionBlockCollection parent(Block_ItemCollection parent) {
            if (this.parent != null)
                throw new IllegalStateException("Attempted to set parent with existing parent! New: " + parent + " Old: " + this.parent);
            this.parent = parent;
            return this;
        }

        @Nullable
        public Block_ItemCollection getParent() {
            return this.parent;
        }
    }

    public static class Builder {
        public static Block_ItemCollection metal(String name, String group, Item.Properties item, ItemGroup blockGroup, AbstractBlock.Properties block) {
            return noOreMetal(name, group, item, blockGroup, block)
                    .put("ore", new CollectionBlockCollection(new Block(block), blockGroup, name, group));
        }

        public static Block_ItemCollection noOreMetal(String name, String group, Item.Properties item, ItemGroup blockGroup, AbstractBlock.Properties block) {
            return new Block_ItemCollection(name, group)
                    .put("ingot", new CollectionItemEntry(new Item(item), name, group))
                    .put("nugget", new CollectionItemEntry(new Item(item), name, group))
                    .put("storage", new CollectionBlockCollection(new Block(block), blockGroup, name, group));
        }
    }
}
