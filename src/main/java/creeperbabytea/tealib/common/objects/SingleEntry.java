package creeperbabytea.tealib.common.objects;

import creeperbabytea.tealib.registry.GeneralDeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

public class SingleEntry<E extends IForgeRegistryEntry<E>> extends AbstractRegistrableEntry<SingleEntry<E>> implements Supplier<E> {
    private final E entry;

    public SingleEntry(E entry, String name, String group) {
        super(name, group);
        this.entry = entry;
    }

    @Override
    public SingleEntry<E> register(GeneralDeferredRegister register) {
        register.register(this.getName(), this.entry, getType());
        return this;
    }

    @SuppressWarnings("unchecked")
    public Class<E> getType() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        return  (Class<E>) pt.getActualTypeArguments()[0];
    }

    public E get() {
        return entry;
    }
}
