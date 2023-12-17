package creeperbabytea.tealib.registry;

import creeperbabytea.tealib.TeaLib;
import creeperbabytea.tealib.util.IModResourceHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@link DeferredRegister} is trash
 * If you want to use some of your own logic to implement the registration, you can inherit this class.
 * The subclass must have a common single-parameter constructor with a String modid.
 * Then you need to run the {@link #init} method of TeaRegister when the main mod class loads to register the registrar.
 */
public class TeaRegister<T extends IForgeRegistryEntry<T>> implements IModResourceHelper {
    public static final Map<Class<?>, Function<String, ? extends TeaRegister<?>>> TEA_SUB_REGISTERS = new HashMap<>();
    public static final Map<String, Map<Class<?>, Function<String, ? extends TeaRegister<?>>>> MOD_SUB_REGISTERS = new HashMap<>();

    protected final Class<T> type;
    protected final String modid;
    protected final Map<ResourceLocation, T> entries = new LinkedHashMap<>();

    protected boolean eventFired = false;

    protected TeaRegister(Class<T> type, String modid) {
        this.type = type;
        this.modid = modid;
    }

    protected TeaRegister(IForgeRegistry<T> type, String modid) {
        this(type.getRegistrySuperType(), modid);
    }

    protected static <R extends TeaRegister<?>> void subRegister(String modid, Class<? extends IForgeRegistryEntry<?>> type, Class<R> register) {
        if (modid.equals(TeaLib.MODID)) {
            TEA_SUB_REGISTERS.put(type, s -> {
                try {
                    return register.getConstructor(String.class).newInstance(s);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("Sub register constructor error: " + register);
                }
            });
        }

        Map<Class<?>, Function<String, ? extends TeaRegister<?>>> map;
        if (MOD_SUB_REGISTERS.containsKey(modid)) {
            map = MOD_SUB_REGISTERS.get(modid);
        } else {
            map = new HashMap<>();
            MOD_SUB_REGISTERS.put(modid, map);
        }
        map.put(type, (s) -> {
            try {
                return register.getConstructor(String.class).newInstance(s);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException("Sub register constructor error: " + register);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends IForgeRegistryEntry<T>> TeaRegister<T> create(Class<T> type, String modid) {
        if (TEA_SUB_REGISTERS.containsKey(type))
            return (TeaRegister<T>) TEA_SUB_REGISTERS.get(type).apply(modid);

        if (MOD_SUB_REGISTERS.containsKey(modid)) {
            Map<Class<?>, Function<String, ? extends TeaRegister<?>>> map = MOD_SUB_REGISTERS.get(modid);
            if (map.containsKey(type))
                return (TeaRegister<T>) map.get(type).apply(modid);
        }
        return new TeaRegister<>(type, modid);
    }

    public static <T extends IForgeRegistryEntry<T>> TeaRegister<T> create(IForgeRegistry<T> reg, String modid) {
        return create(reg.getRegistrySuperType(), modid);
    }


    public <E extends T> E add(ResourceLocation regName, E entry) {
        if (eventFired)
            throw new IllegalStateException("Cannot register new entries to TeaRegister after RegistryEvent.Register has been fired.");
        if (entry.getRegistryName() != null)
            this.entries.put(entry.getRegistryName(), entry);
        else {
            entry.setRegistryName(regName);
            this.entries.put(entry.getRegistryName(), entry);
        }
        return entry;
    }

    public <E extends T> E add(String path, E entry) {
        return this.add(modLoc(path), entry);
    }

    public void register(IEventBus mod) {
        mod.addListener(this::newRegistry);
        mod.register(new EventHandler(this));
    }

    public Collection<T> getEntries() {
        return this.entries.values();
    }

    @Nullable
    public T byKey(ResourceLocation key) {
        return this.entries.get(key);
    }

    /* -------------------- CUSTOM REGISTRY STARTS -------------------- */

    protected RegistryBuilder<T> builder;
    protected IForgeRegistry<T> customReg;

    /** Use the method {@link Supplier#get} after the {@link RegistryEvent.NewRegistry} event is published, otherwise you will get a null */
    public Supplier<IForgeRegistry<T>> makeReg(String path, RegistryBuilder<T> builder) {
        this.builder = builder.setName(modLoc(path)).setType(type);

        return () -> this.customReg;
    }

    public void newRegistry(RegistryEvent.NewRegistry event) {
        if (builder != null) {
            try {
                this.customReg = builder.create();
            } catch (IllegalArgumentException e) {
                TeaLib.LOGGER.error(e, e);
            }
        }
    }

    /* -------------------- CUSTOM REGISTRY ENDS -------------------- */

    @SuppressWarnings("unchecked")
    public void onRegistry(RegistryEvent.Register<?> event) {
        if (event.getGenericType() == this.type) {
            this.eventFired = true;
            this.entries.forEach((key, t) -> {
                IForgeRegistry<T> reg = (IForgeRegistry<T>) event.getRegistry();
                reg.register(t);
            });
        }
    }

    private class EventHandler {
        private final TeaRegister<T> teaRegister;

        private EventHandler(TeaRegister<T> teaRegister) {
            this.teaRegister = teaRegister;
        }

        @SubscribeEvent
        public void onRegistry(RegistryEvent.Register<?> event) {
            teaRegister.onRegistry(event);
        }
    }

    @Override
    public String modId() {
        return this.modid;
    }

    public static void init(String modid, Class<? extends IForgeRegistryEntry<?>> type, Class<? extends TeaRegister<?>> reg) {
        if (!Objects.equals(reg.getCanonicalName(), TeaRegister.class.getCanonicalName()))
            subRegister(modid, type, reg);
    }

    static {
        SubRegisters.init();
    }
}
