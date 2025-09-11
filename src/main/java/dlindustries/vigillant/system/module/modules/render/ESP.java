package dlindustries.vigilant.system.module.modules.render;

import dlindustries.vigilant.system.module.Category;
import dlindustries.vigilant.system.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.*;

public class ESP extends Module {
    private static ESP INSTANCE;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    
    public boolean players = true;
    public boolean mobs = false;
    public boolean animals = false;
    public float lineWidth = 1.5f;
    public Color color = new Color(255, 0, 0, 150);
    
    public ESP() {
        super("ESP", "See entities through walls", Category.RENDER);
        INSTANCE = this;
    }
    
    public static ESP getInstance() {
        return INSTANCE;
    }
    
    public void onWorldRender(MatrixStack matrices, float tickDelta) {
        if (mc.world == null || mc.player == null || !isEnabled()) return;
        
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.lineWidth(lineWidth);
        
        RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        
        for (Entity entity : mc.world.getEntities()) {
            if (shouldRenderEntity(entity)) {
                renderESPBox(entity, matrices, tickDelta);
            }
        }
        
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
    
    private boolean shouldRenderEntity(Entity entity) {
        if (entity == mc.player) return false;
        if (entity.isInvisible()) return false;
        
        if (entity instanceof PlayerEntity) {
            return players;
        }
        return false;
    }
    
    private void renderESPBox(Entity entity, MatrixStack matrices, float tickDelta) {
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        double x = entity.prevX + (entity.getX() - entity.prevX) * tickDelta - cameraPos.x;
        double y = entity.prevY + (entity.getY() - entity.prevY) * tickDelta - cameraPos.y;
        double z = entity.prevZ + (entity.getZ() - entity.prevZ) * tickDelta - cameraPos.z;
        
        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ())
                .offset(x, y, z);
        
        renderBoxOutline(matrices, box);
    }
    
    private void renderBoxOutline(MatrixStack matrices, Box box) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        
        // Bottom rectangle
        addLine(buffer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
        addLine(buffer, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
        addLine(buffer, matrix, box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ);
        addLine(buffer, matrix, box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ);
        
        // Top rectangle
        addLine(buffer, matrix, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
        addLine(buffer, matrix, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
        addLine(buffer, matrix, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ);
        addLine(buffer, matrix, box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ);
        
        // Vertical lines
        addLine(buffer, matrix, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
        addLine(buffer, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
        addLine(buffer, matrix, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        addLine(buffer, matrix, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
        
        tessellator.draw();
    }
    
    private void addLine(BufferBuilder buffer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2) {
        buffer.vertex(matrix, (float)x1, (float)y1, (float)z1);
        buffer.vertex(matrix, (float)x2, (float)y2, (float)z2);
    }
}