package dlindustries.vigilant.system.mixin;

import dlindustries.vigilant.system.module.ModuleManager;
import dlindustries.vigilant.system.module.modules.render.ESP;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRenderMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, 
                        Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, 
                        Matrix4f projectionMatrix, CallbackInfo ci) {
        
        ESP espModule = ModuleManager.getModule(ESP.class);
        if (espModule != null && espModule.isEnabled()) {
            espModule.onWorldRender(matrices, tickDelta);
        }
    }
}