@Mixin(WorldRenderer.class)
public class WorldRenderMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, 
                        Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, 
                        Matrix4f projectionMatrix, CallbackInfo ci) {
        
        // Access through your system instance
        ESP espModule = system.INSTANCE.getModuleManager().getModule(ESP.class);
        if (espModule != null && espModule.isEnabled()) {
            espModule.onWorldRender(matrices, tickDelta);
        }
    }
}