package creeperbabytea.tealib.registry;

import creeperbabytea.tealib.common.objects.AbstractRegistrableEntry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class GeneralRegister {
    private static final Map<String, GeneralRegister> INSTANCES = new HashMap<>();

    public static GeneralRegister create(String modid) {
        if (INSTANCES.containsKey(modid))
            return INSTANCES.get(modid);
        else {
            GeneralRegister ret = new GeneralRegister(modid);
            INSTANCES.put(modid, ret);
            return ret;
        }
    }

    private final String modid;
    private final Map<Class<?>, TeaRegister<?>> REGISTERS = new HashMap<>();

    private GeneralRegister(String modid) {
        this.modid = modid;
    }

    @SuppressWarnings("unchecked")
    public <T extends IForgeRegistryEntry<T>> TeaRegister<T> getRegister(Class<T> clazz) {
        if (REGISTERS.containsKey(clazz))
            return (TeaRegister<T>) REGISTERS.get(clazz);
        else {
            TeaRegister<T> ret = TeaRegister.create(clazz, this.modid);
            REGISTERS.put(clazz, ret);
            return ret;
        }
    }

    public <T extends IForgeRegistryEntry<T>> TeaRegister<T> getRegister(IForgeRegistry<T> registry) {
        return getRegister(registry.getRegistrySuperType());
    }

    public <RE extends AbstractRegistrableEntry<?>> RE add(RE entry) {
        entry.register(this);
        return entry;
    }

    public <T extends IForgeRegistryEntry<T>> T add(String name, T obj, Class<T> clazz) {
        this.getRegister(clazz).add(name, obj);
        return obj;
    }

    public void register(IEventBus mod) {
        this.REGISTERS.values().forEach(reg -> reg.register(mod));
    }
}
