package creeperbabytea.tealib.registry;

import creeperbabytea.tealib.common.objects.AbstractRegistrableEntry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GeneralDeferredRegister {
    private static final Map<String, GeneralDeferredRegister> INSTANCES = new HashMap<>();

    public static GeneralDeferredRegister create(String modid) {
        if (INSTANCES.containsKey(modid))
            return INSTANCES.get(modid);
        else {
            GeneralDeferredRegister ret = new GeneralDeferredRegister(modid);
            INSTANCES.put(modid, ret);
            return ret;
        }
    }

    private final String modid;
    private final Map<Class<?>, DeferredRegister<?>> REGISTERS = new HashMap<>();

    private GeneralDeferredRegister(String modid) {
        this.modid = modid;
    }

    @SuppressWarnings("unchecked")
    public <T extends IForgeRegistryEntry<T>> DeferredRegister<T> getRegister(Class<T> clazz) {
        if (REGISTERS.containsKey(clazz))
            return (DeferredRegister<T>) REGISTERS.get(clazz);
        else {
            DeferredRegister<T> ret = DeferredRegister.create(clazz, this.modid);
            REGISTERS.put(clazz, ret);
            return ret;
        }
    }

    public <T extends IForgeRegistryEntry<T>> DeferredRegister<T> getRegister(IForgeRegistry<T> registry) {
        return getRegister(registry.getRegistrySuperType());
    }

    public <RE extends AbstractRegistrableEntry<?>> RE register(RE entry) {
        entry.register(this);
        return entry;
    }

    public <T extends IForgeRegistryEntry<T>> T register(String name, T obj, Class<T> clazz) {
        this.getRegister(clazz).register(name, () -> obj);
        return obj;
    }

    public void register(IEventBus mod) {
        this.REGISTERS.values().forEach(reg -> reg.register(mod));
    }
}
