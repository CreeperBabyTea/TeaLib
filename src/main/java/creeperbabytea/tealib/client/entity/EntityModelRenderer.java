package creeperbabytea.tealib.client.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import creeperbabytea.tealib.client.entity.model.IndependentTextureModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class EntityModelRenderer<E extends Entity> extends EntityRenderer<E> {
    private final ResourceLocation LOC;
    private final Set<EntityModel<E>> MODELS = new HashSet<>();

    public EntityModelRenderer(EntityRendererManager renderManager, ResourceLocation loc, EntityModel<E>... model) {
        super(renderManager);
        LOC = loc;
        MODELS.addAll(Arrays.asList(model));
    }

    @Override
    public void render(E entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        MODELS.forEach(model -> {
            IVertexBuilder builder;
            if (model instanceof IndependentTextureModel)
                builder = bufferIn.getBuffer(model.getRenderType(((IndependentTextureModel<E>) model).getTexture()));
            else
                builder = bufferIn.getBuffer(model.getRenderType(this.getEntityTexture(entityIn)));
            model.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        });
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(E entity) {
        return this.LOC;
    }
}
