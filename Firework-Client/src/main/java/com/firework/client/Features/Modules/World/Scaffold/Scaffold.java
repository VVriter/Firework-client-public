package com.firework.client.Features.Modules.World.Scaffold;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "Scaffold", category = Module.Category.WORLD)
public class Scaffold extends Module {

    Timer timer = new Timer();

    public Setting<Boolean> rotate  = new Setting<>("Rotates", true, this);

    public Setting<Boolean> swing  = new Setting<>("Swing", true, this);

    public Setting<Boolean> Switch  = new Setting<>("Switch", true, this);

    public Setting<Boolean> Tower  = new Setting<>("Tower", true, this);

    public Setting<Double> speed = new Setting<>("Delay", (double)0.7, this, 0, 1);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();

    private BlockPos pos;

    private boolean packet = false;



    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
    }


    @Override
    public void onTick() {
        super.onTick();
        pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (isAir(pos)) {
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet, mc.player.isSneaking());
            blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(pos)));
        }

        if(swing.getValue()){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }


        if (mc.player == null || mc.world == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        mc.player.motionX = calc[0];
        mc.player.motionZ = calc[1];


        if (Switch.getValue() && (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) )))
            for (int j = 0; j < 9; j++) {
                if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock ) {
                    mc.player.inventory.currentItem = j;
                    break;
                }
            }

        if(Tower.getValue() && mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isElytraFlying() && timer.hasPassedMs(1000)){
         /*   if(mc.gameSettings.keyBindJump.isKeyDown() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F&& !mc.player.isPotionActive(MobEffects.JUMP_BOOST)){
                mc.player.motionY = 0.2444441D;
                mc.player.motionZ = 0.0D;
                mc.player.motionX = 0.0D;
            } */


         /*   Scaffold.mc.player.motionX *= 0.3;
            Scaffold.mc.player.motionZ *= 0.3;
            //Scaffold.mc.player.jump();
            mc.player.motionY = 0.2444441D;
            if (this.timer.hasPassedMs(1500L)) {
                Scaffold.mc.player.motionY = -0.28;
                this.timer.reset();
            } */

            if (timer.hasPassedMs(3000)) {
                timer.reset();
                mc.player.motionY = -0.28f;
            } else {
                final float towerMotion = 0.2199999f;

                mc.player.setVelocity(0, towerMotion, 0);
            }

        }
    }

    private boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
        blocksToRender.clear();
    }
}
