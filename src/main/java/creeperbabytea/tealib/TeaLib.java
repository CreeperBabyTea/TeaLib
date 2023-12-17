package creeperbabytea.tealib;

import com.mojang.serialization.Codec;
import creeperbabytea.tealib.registry.SubRegisters;
import creeperbabytea.tealib.registry.TeaParticleRegister;
import creeperbabytea.tealib.registry.TeaRegister;
import net.minecraft.client.particle.AshParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@Mod(TeaLib.MODID)
public class TeaLib {
    public static final String MODID = "tea_lib";
    public static final Logger LOGGER = LogManager.getLogger();

    public static TeaLib INSTANCE;

    public static IEventBus MOD;
    public static IEventBus FORGE;

    public TeaLib() {
        INSTANCE = this;

        MOD = FMLJavaModLoadingContext.get().getModEventBus();
        FORGE = MinecraftForge.EVENT_BUS;

        SubRegisters.init();

        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }
}
