package com.GreenWizard.MouseControl;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class MyHandler {

	private static final float MAX_DISTANCE = 8.0f;
	private boolean isRunning = false;
	private boolean isLMBDown = false;
	private Minecraft client;

	public MyHandler() {
		client = Minecraft.getMinecraft();
	}

	private void setRunningMode(Boolean isRunning_) {
		isRunning = isRunning_;
		KeyBinding key = client.gameSettings.keyBindForward;
		key.setKeyBindState(key.getKeyCode(), isRunning);
		if (!isRunning)
			setJumping(false);
	}

	private void setJumping(Boolean needJump) {
		KeyBinding jump = client.gameSettings.keyBindJump;
		jump.setKeyBindState(jump.getKeyCode(), needJump);
	}

	// Called when the client ticks.
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (isRunning && (event.phase == Phase.END)) {
			if ((client.theWorld == null) || (client.currentScreen != null))
				return;

			EntityClientPlayerMP player = client.thePlayer;
			Boolean needJump = player.isInWater(); // always jump in water
			if (player.onGround)
				if (player.isCollidedHorizontally && !player.isOnLadder())
					needJump = true;
			setJumping(needJump);
		}
	}

	@SubscribeEvent
	public void onMouseEvent(MouseInputEvent event) {
		if ((client.theWorld == null) || (client.currentScreen != null))
			return;

		boolean LMB = Mouse.isButtonDown(0);
		if (isRunning) {
			setRunningMode(LMB);
		} else {
			if (LMB && !isLMBDown) {
				MovingObjectPosition LA = client.renderViewEntity.rayTrace(MAX_DISTANCE, 1.0f);

				switch (LA.typeOfHit) {
				case BLOCK:
					if (client.theWorld.canMineBlock(client.thePlayer, LA.blockX, LA.blockY, LA.blockZ))
						break;
				case MISS:
					setRunningMode(true);
					KeyBinding mouseBtn = client.gameSettings.keyBindAttack;
					mouseBtn.setKeyBindState(mouseBtn.getKeyCode(), false);
					break;
					
				default:
					break;
				}
			}
			isLMBDown = LMB;
		}
	}
}
