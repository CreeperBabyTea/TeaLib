package creeperbabytea.tealib.common.objects;

import javax.annotation.Nullable;

public abstract class AbstractCollectionEntry<CE extends AbstractCollectionEntry<?>> extends AbstractRegistrableEntry<AbstractCollectionEntry<CE>> {
    public AbstractCollectionEntry(String name, String group) {
        super(name, group);
    }

    public abstract CE put(String key, AbstractRegistrableEntry<?> value);

    @Nullable
    public abstract AbstractRegistrableEntry<?> get(String key);
}
