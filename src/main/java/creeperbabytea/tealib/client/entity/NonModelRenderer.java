package creeperbabytea.tealib.client.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NonModelRenderer<E extends Entity> extends EntityRenderer<E> {
    private static final ResourceLocation EMPTY = new ResourceLocation("empty");

    public NonModelRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(E entity) {
        return EMPTY;
    }
}
