package creeperbabytea.tealib.common.registry;

import creeperbabytea.tealib.TeaLib;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.util.TriConsumer;

public class SubRegisters {
    public static void init() {
        init(TeaParticleRegister::init, ForgeRegistries.PARTICLE_TYPES.getRegistrySuperType(), TeaParticleRegister.class);
    }

    private static void init(TriConsumer<String, Class<? extends IForgeRegistryEntry<?>>, Class<? extends TeaRegister<?>>> method,
                             Class<? extends IForgeRegistryEntry<?>> type,
                             Class<? extends TeaRegister<?>> reg) {
        method.accept(TeaLib.MODID, type, reg);
    }
}
