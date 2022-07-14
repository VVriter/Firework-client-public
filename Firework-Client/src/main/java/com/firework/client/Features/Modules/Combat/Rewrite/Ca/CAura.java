package com.firework.client.Features.Modules.Combat.Rewrite.Ca;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@ModuleManifest(name = "CAura",category = Module.Category.COMBAT)
public class CAura extends Module {

    public Setting<Integer> range = new Setting<>("Range", 5, this, 1, 10);
    public Setting<Boolean> legal = new Setting<>("Legal", true, this);

    public Setting<Integer> placeDelay = new Setting<>("PaceDelay", 200, this, 1, 1000);
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);

    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 5, this, 1, 20);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 5, this, 1, 20);

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this, ItemUser.switchModes.values());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Rotate", true, this);

    Timer placeTimer;
    Timer breakTimer;

    EntityPlayer target;

    ItemUser user;

    @Override public void onEnable() {super.onEnable();
        placeTimer = new Timer();
        breakTimer = new Timer();
        user = new ItemUser(this,switchMode,rotate);
    }

    @Override public void onDisable() {super.onDisable();
        placeTimer = null;
        breakTimer = null;
        user = null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        target = PlayerUtil.getClosestTarget(range.getValue());
    }

    BlockPos posToPlace;
    @Override public void onTick() {super.onTick();
        posToPlace = CrystalUtils.bestCrystalPos(target,range.getValue(),legal.getValue());

        if (target != null && posToPlace != null) {
            //AutoCrystal code
            if (placeTimer.hasPassedMs(placeDelay.getValue())) {
                user.useItem(Items.END_CRYSTAL,posToPlace, EnumHand.MAIN_HAND, packet.getValue());
                placeTimer.reset();
            }
        }
    }

    @SubscribeEvent public void onRender(RenderWorldLastEvent e) {
        if (target != null) {
            RenderUtils.drawProperBox(target.getPosition(),new Color(203, 3, 3,200));
            RenderUtils.drawBoxESP(CrystalUtils.bestCrystalPos(target,range.getValue(),legal.getValue()),Color.BLUE,1,true,true,200,1);
        }
    }
}
