package dlindustries.vigillant.system.mixin;

import dlindustries.vigillant.system.module.modules.render.ESP;
import dlindustries.vigillant.system.system;
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
        
        // Get the ESP module from your module manager
        ESP espModule = system.INSTANCE.getModuleManager().getModule(ESP.class);
        
        // If ESP module exists and is enabled, call its render method
        if (espModule != null && espModule.isEnabled()) {
            espModule.onWorldRender(matrices, tickDelta);
        }
    }
}