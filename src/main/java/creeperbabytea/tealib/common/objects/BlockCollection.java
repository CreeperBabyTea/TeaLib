package creeperbabytea.tealib.common.objects;

import creeperbabytea.tealib.registry.GeneralDeferredRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;

public class BlockCollection extends AbstractCollectionEntry<BlockCollection> implements IItemProvider {
    private final SingleBlockEntry block;
    private final SingleItemEntry blockItem;

    public BlockCollection(Block block, Item.Properties blockItem, String name, String group) {
        super(name, group);
        this.block = new SingleBlockEntry(block, name, group);
        this.blockItem = new SingleItemEntry(new BlockItem(block, blockItem), name, group);
    }

    public BlockCollection(Block block, ItemGroup tab, String name, String group) {
        this(block, new Item.Properties().group(tab), name, group);
    }

    public BlockCollection(AbstractBlock.Properties block, ItemGroup tab, String name) {
        this(new Block(block), tab, name, "");
    }

    @Deprecated
    @Override
    public BlockCollection put(String key, AbstractRegistrableEntry<?> value) {
        throw new IllegalArgumentException("Cannot modify the contents of a BlockCollection!");
    }

    @Nullable
    @Override
    public AbstractRegistrableEntry<?> get(String key) {
        if (key.equals("block")) return this.block;
        else if (key.contains("item")) return this.blockItem;
        else return null;
    }

    public SingleBlockEntry getBlock() {
        return block;
    }

    public SingleItemEntry getItem() {
        return blockItem;
    }

    @Override
    public BlockCollection register(GeneralDeferredRegister register) {
        register.register(this.block);
        register.register(this.blockItem);
        return this;
    }

    @Override
    public Item asItem() {
        return this.blockItem.get();
    }

    public static class SingleBlockEntry extends SingleEntry<Block> {
        public SingleBlockEntry(Block entry, String name, String group) {
            super(entry, name, group);
        }

        @Override
        public Class<Block> getType() {
            return Block.class;
        }
    }
}
