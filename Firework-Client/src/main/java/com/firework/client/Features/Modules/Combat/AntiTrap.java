package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Entity.TargetUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "AntiTrap",
        category = Module.Category.COMBAT,
        description = "Places crystals to prevent trapping"
)
public class AntiTrap extends Module {

    public Setting<Mode> mode = new Setting<>("Mode", Mode.Always, this);
    public Setting<Double> distance = new Setting<>("Distance", (double)3, this, 1, 20).setVisibility(v-> mode.getValue(Mode.Smart));
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Integer> delay = new Setting<>("DelayMs", 3, this, 1, 1000).setVisibility(v-> interaction.getValue());;

    public Setting<Boolean> autoDisable = new Setting<>("AutoDisable", false, this);

    ItemUser itemUser;
    Timer timer;

    Entity target;

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new Timer();
        timer.reset();

        itemUser = new ItemUser(this, switchMode, rotate);
    }


    @Override
    public void onDisable() {
        super.onDisable();
        timer = null;

        itemUser = null;
    }


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e-> {
        BlockPos pos = EntityUtil.getPlayerPos(mc.player);
        target = TargetUtil.getClosest(true,false,false,false,false,100);
        switch (mode.getValue()) {
            case Always:
                if (CrystalUtils.canPlaceCrystal(pos.add(1,0,0))) {
                    //BlockUtil.placeCrystalOnBlock(pos.add(1,0,0),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                    if (timer.hasPassedMs(delay.getValue())) {
                        itemUser.useItem(Items.END_CRYSTAL,pos.add(1,0,0),EnumHand.MAIN_HAND);
                        timer.reset();
                    }
                } else if (CrystalUtils.canPlaceCrystal(pos.add(-1,0,0))) {
                    //BlockUtil.placeCrystalOnBlock(pos.add(-1,0,0),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                    if (timer.hasPassedMs(delay.getValue())) {
                        itemUser.useItem(Items.END_CRYSTAL, pos.add(-1, 0, 0), EnumHand.MAIN_HAND);
                        timer.reset();
                    }
                } else if (CrystalUtils.canPlaceCrystal(pos.add(0,0,1))) {
                    //BlockUtil.placeCrystalOnBlock(pos.add(0,0,1),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                    if (timer.hasPassedMs(delay.getValue())) {
                        itemUser.useItem(Items.END_CRYSTAL,pos.add(0,0,1),EnumHand.MAIN_HAND);
                        timer.reset();
                    }
                } else if (CrystalUtils.canPlaceCrystal(pos.add(0,0,-1))) {
                    //BlockUtil.placeCrystalOnBlock(pos.add(0,0,-1),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                    if (timer.hasPassedMs(delay.getValue())) {
                        itemUser.useItem(Items.END_CRYSTAL,pos.add(0,0,-1),EnumHand.MAIN_HAND);
                        timer.reset();
                    }
                } else {
                    MessageUtil.sendError("Unable to make anti trap lul",-1117);
                    if (autoDisable.getValue())
                        onDisable();
                }
                break;
            case Smart:
                if (target != null) {
                    if (mc.player.getDistanceSq(target) <= distance.getValue()) {
                        if (CrystalUtils.canPlaceCrystal(pos.add(1,0,0))) {
                            //BlockUtil.placeCrystalOnBlock(pos.add(1,0,0),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                            if (timer.hasPassedMs(delay.getValue())) {
                                itemUser.useItem(Items.END_CRYSTAL,pos.add(1,0,0),EnumHand.MAIN_HAND);
                                timer.reset();
                            }
                        } else if (CrystalUtils.canPlaceCrystal(pos.add(-1,0,0))) {
                            //BlockUtil.placeCrystalOnBlock(pos.add(-1,0,0),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                            if (timer.hasPassedMs(delay.getValue())) {
                                itemUser.useItem(Items.END_CRYSTAL, pos.add(-1, 0, 0), EnumHand.MAIN_HAND);
                                timer.reset();
                            }
                        } else if (CrystalUtils.canPlaceCrystal(pos.add(0,0,1))) {
                            //BlockUtil.placeCrystalOnBlock(pos.add(0,0,1),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                            if (timer.hasPassedMs(delay.getValue())) {
                                itemUser.useItem(Items.END_CRYSTAL,pos.add(0,0,1),EnumHand.MAIN_HAND);
                                timer.reset();
                            }
                        } else if (CrystalUtils.canPlaceCrystal(pos.add(0,0,-1))) {
                            //BlockUtil.placeCrystalOnBlock(pos.add(0,0,-1),EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue(),rotate.getValue());
                            if (timer.hasPassedMs(delay.getValue())) {
                                itemUser.useItem(Items.END_CRYSTAL,pos.add(0,0,-1),EnumHand.MAIN_HAND);
                                timer.reset();
                            }
                        } else {
                            MessageUtil.sendError("Unable to make anti trap lul",-1117);
                            if (autoDisable.getValue())
                                onDisable();
                        }
                    }
                }
                break;
        }
    });


    @Subscribe
    public Listener<Render3dE> ec = new Listener<>(e-> {
        BlockPos pos = EntityUtil.getPlayerPos(mc.player);
        RenderUtils.drawBoxESP(pos, Color.CYAN,1,true,true,160,1);
    });


    public enum Mode{
        Always, Smart
    }
}
