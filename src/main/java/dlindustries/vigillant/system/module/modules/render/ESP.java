package dlindustries.vigilant.system.module.modules.render;

import dlindustries.vigilant.system.module.Category;
import dlindustries.vigilant.system.module.Module;
import dlindustries.vigilant.system.setting.Setting;
import dlindustries.vigilant.system.setting.settings.BooleanSetting;
import dlindustries.vigilant.system.setting.settings.ColorSetting;
import dlindustries.vigilant.system.setting.settings.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ESP extends Module {
    private final BooleanSetting players = new BooleanSetting("Players", this, true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", this, false);
    private final BooleanSetting animals = new BooleanSetting("Animals", this, false);
    private final NumberSetting lineWidth = new NumberSetting("Line Width", this, 1.5, 0.5, 5.0, 0.1);
    private final ColorSetting color = new ColorSetting("Color", this, new Color(255, 0, 0, 150));
    
    public ESP() {
        super("ESP", "See entities through walls", Category.RENDER);
        
        // Add settings
        addSetting(players);
        addSetting(mobs);
        addSetting(animals);
        addSetting(lineWidth);
        addSetting(color);
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.world == null || mc.player == null || !isEnabled()) return;
        
        // Store original GL state
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.glLineWidth((float) lineWidth.getValue());
        
        // Set the color from settings
        Color c = color.getValue();
        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        
        // Render ESP for all entities
        for (Entity entity : mc.world.loadedEntityList) {
            if (shouldRenderEntity(entity)) {
                renderESPBox(entity, event.getPartialTicks());
            }
        }
        
        // Restore original GL state
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
    }
    
    private boolean shouldRenderEntity(Entity entity) {
        if (entity == mc.player) return false; // Don't render self
        if (entity.isInvisible()) return false; // Skip invisible entities
        
        if (entity instanceof EntityPlayer) {
            return players.getValue();
        }
        // You would add more entity type checks here for mobs, animals, etc.
        // This is a simplified version
        
        return false;
    }
    
    private void renderESPBox(Entity entity, float partialTicks) {
        // Calculate interpolated position
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
        
        // Entity dimensions
        double width = entity.width / 2.0;
        double height = entity.height;
        
        // Draw the ESP box
        GlStateManager.glBegin(3); // GL_LINE_STRIP
        
        // Bottom rectangle
        GlStateManager.glVertex3d(x - width, y, z - width);
        GlStateManager.glVertex3d(x + width, y, z - width);
        GlStateManager.glVertex3d(x + width, y, z + width);
        GlStateManager.glVertex3d(x - width, y, z + width);
        GlStateManager.glVertex3d(x - width, y, z - width);
        
        // Top rectangle
        GlStateManager.glVertex3d(x - width, y + height, z - width);
        GlStateManager.glVertex3d(x + width, y + height, z - width);
        GlStateManager.glVertex3d(x + width, y + height, z + width);
        GlStateManager.glVertex3d(x - width, y + height, z + width);
        GlStateManager.glVertex3d(x - width, y + height, z - width);
        
        // Vertical lines
        GlStateManager.glVertex3d(x - width, y, z - width);
        GlStateManager.glVertex3d(x - width, y + height, z - width);
        
        GlStateManager.glVertex3d(x + width, y, z - width);
        GlStateManager.glVertex3d(x + width, y + height, z - width);
        
        GlStateManager.glVertex3d(x + width, y, z + width);
        GlStateManager.glVertex3d(x + width, y + height, z + width);
        
        GlStateManager.glVertex3d(x - width, y, z + width);
        GlStateManager.glVertex3d(x - width, y + height, z + width);
        
        GlStateManager.glEnd();
    }
}