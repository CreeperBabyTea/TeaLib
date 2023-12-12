package creeperbabytea.tealib.util;

import net.minecraft.util.ResourceLocation;

public interface IModResourceHelper {
    String modId();

    default ResourceLocation modLoc(String path) {
        return new ResourceLocation(modId(), path);
    }

    default ResourceLocation mcLoc(String path) {
        return new ResourceLocation(path);
    }

    default ResourceLocation forgeLoc(String path) {
        return new ResourceLocation("forge", path);
    }
}
