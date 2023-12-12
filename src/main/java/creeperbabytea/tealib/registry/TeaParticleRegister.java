package creeperbabytea.tealib.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TeaParticleRegister extends TeaRegister<ParticleType<?>> {
    private final Map<ParticleType<?>, ParticleManager.IParticleMetaFactory<?>> FACTORIES = new HashMap<>();

    public TeaParticleRegister(String modid) {
        super(ForgeRegistries.PARTICLE_TYPES, modid);
    }

    @SuppressWarnings("unchecked")
    public <T extends IParticleData> ParticleType<T> add(ResourceLocation regName, ParticleType<T> entry, ParticleManager.IParticleMetaFactory<T> factory) {
        this.FACTORIES.put(entry, factory);
        return (ParticleType<T>) super.add(regName, entry);
    }

    @SuppressWarnings("unchecked")
    public <T extends IParticleData> ParticleType<T> add(String path, ParticleType<T> entry, ParticleManager.IParticleMetaFactory<T> factory) {
        this.FACTORIES.put(entry, factory);
        return (ParticleType<T>) super.add(modLoc(path), entry);
    }

    @Deprecated
    @Override
    public ParticleType<?> add(ResourceLocation regName, ParticleType<?> entry) {
        if (checkAdd(entry))
            return super.add(regName, entry);
        else
            throw new IllegalStateException("Use the method that binds the particle with a factory");
    }

    @Deprecated
    @Override
    public ParticleType<?> add(String path, ParticleType<?> entry) {
        if (checkAdd(entry))
            return super.add(modLoc(path), entry);
        else
            throw new IllegalStateException("Use the method that binds the particle with a factory");
    }

    @SuppressWarnings("unchecked")
    private <T extends IParticleData> boolean checkAdd(ParticleType<T> entry) {
        if (entry instanceof Supplier && ((Supplier<?>) entry).get() instanceof ParticleManager.IParticleMetaFactory) {
            ParticleManager.IParticleMetaFactory<T> factory = (ParticleManager.IParticleMetaFactory<T>) ((Supplier<?>) entry).get();
            this.FACTORIES.put(entry, factory);
            return true;
        }
        return false;
    }

    @Override
    public void register(IEventBus mod) {
        super.register(mod);
        mod.addListener(this::onParticleFactoryRegister);
    }

    public void onParticleFactoryRegister(ParticleFactoryRegisterEvent event) {
        this.entries.forEach((key, particle) -> {
            registerFactory(particle);
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends IParticleData> ParticleManager.IParticleMetaFactory<T> getParticleFactory(ParticleType<T> particle) {
        return (ParticleManager.IParticleMetaFactory<T>) FACTORIES.get(particle);
    }

    private <T extends IParticleData> void registerFactory(ParticleType<T> particle) {
        Minecraft.getInstance().particles.registerFactory(particle, getParticleFactory(particle));
    }
}
