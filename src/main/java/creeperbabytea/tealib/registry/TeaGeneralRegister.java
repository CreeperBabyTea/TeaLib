package creeperbabytea.tealib.registry;

import creeperbabytea.tealib.common.objects.AbstractRegistrableEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class TeaGeneralRegister {
    private static final Map<String, TeaGeneralRegister> INSTANCES = new HashMap<>();

    public static TeaGeneralRegister create(String modid) {
        if (INSTANCES.containsKey(modid))
            return INSTANCES.get(modid);
        else {
            TeaGeneralRegister ret = new TeaGeneralRegister(modid);
            INSTANCES.put(modid, ret);
            return ret;
        }
    }

    private final String modid;
    private final Map<Class<?>, TeaRegister<?>> REGISTERS = new HashMap<>();

    private TeaGeneralRegister(String modid) {
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

    private Class<? extends IForgeRegistryEntry<?>> currentSuperType;

    public <RE extends AbstractRegistrableEntry<?>> RE add(RE entry) {
        entry.register(this);
        return entry;
    }

    public <T extends IForgeRegistryEntry<T>> T add(String name, T obj, Class<T> clazz) {
        getRegister(clazz).add(name, obj);
        bindSuperClass(clazz);
        return obj;
    }

    public <T extends IForgeRegistryEntry<T>> T add(ResourceLocation regName, T obj, Class<T> clazz) {
        getRegister(clazz).add(regName, obj);
        bindSuperClass(clazz);
        return obj;
    }

    public <T extends IForgeRegistryEntry<T>> T add(ResourceLocation regName, T obj) {
        if (currentSuperType != null)
            add(regName, obj);
        throw new IllegalStateException("Cannot register an entry with an unknown super type: " + obj);
    }

    public <T extends IForgeRegistryEntry<T>> T add(String name, T obj) {
        if (currentSuperType != null)
            add(name, obj);
        throw new IllegalStateException("Cannot register an entry with an unknown super type: " + obj);
    }

    public void bindSuperClass(Class<? extends IForgeRegistryEntry<?>> clazz) {
        this.currentSuperType = clazz;
    }

    public void register(IEventBus mod) {
        this.REGISTERS.values().forEach(reg -> reg.register(mod));
    }
}
