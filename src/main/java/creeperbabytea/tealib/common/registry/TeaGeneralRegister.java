package creeperbabytea.tealib.common.registry;

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
        else
            return new TeaGeneralRegister(modid);
    }

    private final String modid;
    private final Map<Class<?>, TeaRegister<?>> REGISTERS = new HashMap<>();

    private TeaGeneralRegister(String modid) {
        this.modid = modid;
        INSTANCES.put(modid, this);
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

    public <E extends T, T extends IForgeRegistryEntry<T>> E add(String name, E obj, Class<T> clazz) {
        getRegister(clazz).add(name, obj);
        bindSuperClass(clazz);
        return obj;
    }

    public <E extends T, T extends IForgeRegistryEntry<T>> E add(ResourceLocation regName, E obj, Class<T> clazz) {
        getRegister(clazz).add(regName, obj);
        bindSuperClass(clazz);
        return obj;
    }

    @SuppressWarnings("unchecked")
    public <E extends T, T extends IForgeRegistryEntry<T>> E add(ResourceLocation regName, E obj) {
        if (currentSuperType != null && currentSuperType.isInstance(obj)) {
            add(regName, obj, (Class<T>) currentSuperType);
            return obj;
        }
        throw new IllegalStateException("Cannot register an entry with an unknown super type: " + obj);
    }

    @SuppressWarnings("unchecked")
    public <E extends T, T extends IForgeRegistryEntry<T>> E add(String name, E obj) {
        if (currentSuperType != null && currentSuperType.isInstance(obj)) {
            add(name, obj, (Class<T>) currentSuperType);
            return obj;
        }
        throw new IllegalStateException("Cannot register an entry with an unknown super type: " + obj);
    }

    public void bindSuperClass(Class<? extends IForgeRegistryEntry<?>> clazz) {
        this.currentSuperType = clazz;
    }

    public void register(IEventBus mod) {
        this.REGISTERS.values().forEach(reg -> reg.register(mod));
    }
}
