package dlindustries.vigillant.system.module.modules.sword;

import dlindustries.vigillant.system.event.events.AttackListener;
import dlindustries.vigillant.system.event.events.TickListener;
import dlindustries.vigillant.system.module.Category;
import dlindustries.vigillant.system.module.Module;
import dlindustries.vigillant.system.module.setting.BooleanSetting;
import dlindustries.vigillant.system.module.setting.NumberSetting;
import dlindustries.vigillant.system.utils.EncryptedString;
import dlindustries.vigillant.system.utils.InventoryUtils;
import dlindustries.vigillant.system.utils.MouseSimulation;
import dlindustries.vigillant.system.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public final class ShieldDisabler extends Module implements TickListener, AttackListener {
	private final NumberSetting hitDelay = new NumberSetting(EncryptedString.of("Hit Delay"), 0, 20, 0, 1);
	private final NumberSetting switchDelay = new NumberSetting(EncryptedString.of("Switch Delay"), 0, 20, 1, 1);
	private final BooleanSetting switchBack = new BooleanSetting(EncryptedString.of("Switch Back"), true);
	private final BooleanSetting stun = new BooleanSetting(EncryptedString.of("Stun"), false);
	private final BooleanSetting stunSlam = new BooleanSetting(EncryptedString.of("Stun Slam"), true)
			.setDescription(EncryptedString.of("Use mace for second hit instead of sword"));
	private final NumberSetting maceSlot = new NumberSetting(EncryptedString.of("Mace Slot"), 1, 9, 1, 1)
			.setDescription(EncryptedString.of("Slot 1-9 for mace (for Stun Slam)"));
	private final BooleanSetting clickSimulate = new BooleanSetting(EncryptedString.of("Click Simulation"), true);
	private final BooleanSetting requireHoldAxe = new BooleanSetting(EncryptedString.of("Hold Axe"), false);

	private int originalSlot = -1;
	private int hitClock, switchClock;
	private boolean inStunSequence;
	private int stunStep;

	public ShieldDisabler() {
		super(EncryptedString.of("Shield Disabler"),
				EncryptedString.of("Automatically disables your opponents shield"),
				-1,
				Category.sword);

		addSettings(switchDelay, hitDelay, switchBack, stun, stunSlam, maceSlot, clickSimulate, requireHoldAxe);
	}

	@Override
	public void onEnable() {
		eventManager.add(TickListener.class, this);
		eventManager.add(AttackListener.class, this);

		hitClock = hitDelay.getValueInt();
		switchClock = switchDelay.getValueInt();
		originalSlot = -1;
		inStunSequence = false;
		stunStep = 0;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		eventManager.remove(TickListener.class, this);
		eventManager.remove(AttackListener.class, this);
		restoreOriginalSlot();
		super.onDisable();
	}

	@Override
	public void onTick() {
		if (mc.currentScreen != null)
			return;

		if (requireHoldAxe.getValue() && !(mc.player.getMainHandStack().getItem() instanceof AxeItem))
			return;

		if (inStunSequence) {
			handleStunSequence();
			return;
		}

		if (!(mc.crosshairTarget instanceof EntityHitResult entityHit)) {
			if (originalSlot != -1) {
				restoreOriginalSlot();
			}
			return;
		}

		Entity entity = entityHit.getEntity();

		// Don't target yourself or non-living entities
		if (entity == mc.player || !(entity instanceof LivingEntity))
			return;

		if (mc.player.isUsingItem())
			return;

		if (entity instanceof PlayerEntity player) {
			if (WorldUtils.isShieldFacingAway(player))
				return;

			if (player.isHolding(Items.SHIELD) && player.isBlocking()) {
				// Store original slot only once per sequence
				if (originalSlot == -1) {
					originalSlot = mc.player.getInventory().selectedSlot;
				}

				if (switchClock > 0) {
					switchClock--;
					return;
				}

				if (InventoryUtils.selectAxe()) {
					if (hitClock > 0) {
						hitClock--;
					} else {
						// First hit with axe (shield break)
						if (clickSimulate.getValue())
							MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);
						WorldUtils.hitEntity(player, true);

						// Start stun sequence if enabled
						if (stun.getValue()) {
							inStunSequence = true;
							stunStep = 1;
						} else {
							// Reset immediately if no stun
							hitClock = hitDelay.getValueInt();
							switchClock = switchDelay.getValueInt();
							restoreOriginalSlot();
						}
					}
				}
			} else if (originalSlot != -1) {
				restoreOriginalSlot();
			}
		}
	}

	private void handleStunSequence() {
		if (mc.player == null) {
			resetSequence();
			return;
		}

		switch (stunStep) {
			case 1: // Prepare for second hit
				if (stunSlam.getValue()) {
					// Switch to mace for slam
					mc.player.getInventory().selectedSlot = maceSlot.getValueInt() - 1;
				}
				stunStep = 2;
				break;

			case 2: // Execute second hit
				// Get current target fresh
				if (mc.crosshairTarget instanceof EntityHitResult entityHit) {
					Entity entity = entityHit.getEntity();
					if (entity != null) {
						if (clickSimulate.getValue())
							MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);
						WorldUtils.hitEntity(entity, true);
					}
				}
				stunStep = 3;
				break;

			case 3: // Restore to original weapon
				restoreOriginalSlot();
				resetSequence();
				break;
		}
	}

	private void resetSequence() {
		inStunSequence = false;
		stunStep = 0;

		// Reset main timers
		hitClock = hitDelay.getValueInt();
		switchClock = switchDelay.getValueInt();
	}

	private void restoreOriginalSlot() {
		if (switchBack.getValue() && originalSlot != -1) {
			// Add small delay for natural look
			if (switchDelay.getValueInt() > 0) {
				if (switchClock > 0) {
					switchClock--;
				} else {
					mc.player.getInventory().selectedSlot = originalSlot;
					originalSlot = -1;
				}
			} else {
				mc.player.getInventory().selectedSlot = originalSlot;
				originalSlot = -1;
			}
		}
	}

	@Override
	public void onAttack(AttackEvent event) {
		if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) != GLFW.GLFW_PRESS)
			event.cancel();
	}
}