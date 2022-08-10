package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Step",
        category = Module.Category.MOVEMENT,
        description = "Allows you to travel up on blocks"
)
public class Step extends Module {

    public Setting<modes> mode = new Setting<>("Mode", modes.Timer, this);
    public enum modes{
        Timer, Strict
    }

    public Setting<Integer> ticks = new Setting<>("Ticks", 0, this, 0, 50).setVisibility(v-> mode.getValue(modes.Timer));

    public Setting<Double> delay = new Setting<>("DelayS", 0d, this, 0, 1).setVisibility(v-> mode.getValue(modes.Timer));

    float defaultTickLeght;
    boolean autoJump;
    boolean reset;

    Timer timer;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) super.onDisable();
        timer = new Timer();
        defaultTickLeght = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
        reset = false;
        autoJump = mc.gameSettings.autoJump;
        mc.gameSettings.autoJump = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.autoJump = autoJump;
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
        timer = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if (mode.getValue(modes.Timer) && reset && mc.player.onGround) {
            reset = false;
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
        }

        if(mode.getValue(modes.Timer) && mc.player.collidedHorizontally && mc.player.onGround &&
                (mc.player.movementInput.forwardKeyDown
                || mc.player.movementInput.leftKeyDown
                || mc.player.movementInput.rightKeyDown
                || mc.player.movementInput.backKeyDown)){

            mc.player.jump();
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght - ticks.getValue().floatValue());
        }

        if(timer.hasPassedMs(delay.getValue()*1000)) {
            reset = true;
            timer.reset();
        }


        if (mode.getValue(modes.Strict)) {
            AxisAlignedBB bb = mc.player.getEntityBoundingBox();;
            for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX + 1.0D); ++x) {
                for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ + 1.0D); ++z) {
                    BlockPos pos1 = new BlockPos(x, bb.maxY + 1.0D, z);
                    BlockPos pos2 = new BlockPos(x, bb.maxY + 2.0D, z);
                    BlockPos pos3 = new BlockPos(mc.player.getPositionVector().x, mc.player.getPositionVector().y + 3.0D, Step.mc.player.getPositionVector().z);
                    if (!BlockUtil.isAir(pos1) || !BlockUtil.isAir(pos2) || !BlockUtil.isAir(pos3)) return;
                }
            }
            if (mc.player.collidedHorizontally && mc.player.onGround) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75D, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16D, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.23D, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.2D, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
            }
        }

    });
}
