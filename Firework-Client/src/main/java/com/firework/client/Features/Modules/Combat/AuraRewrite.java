package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.WeaponUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "AuraRewrite",
        category = Module.Category.COMBAT
)
public class AuraRewrite extends Module {

    public Setting<Double> targetRange = new Setting<>("TargetRange", (double)4, this, 0, 6);

    public Setting<Boolean> interactionsSubBool = new Setting<>("Interact", false, this).setMode(Setting.Mode.SUB);
    public Setting<AttackMode> attackMode = new Setting<>("Mode", AttackMode.CustomDelay, this).setVisibility(v-> interactionsSubBool.getValue());
    public Setting<Boolean> packet = new Setting<>("Packet", true, this).setVisibility(v-> interactionsSubBool.getValue() && (attackMode.getValue(AttackMode.Old) || attackMode.getValue(AttackMode.CustomDelay)));
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(v-> interactionsSubBool.getValue() && (attackMode.getValue(AttackMode.Old) || attackMode.getValue(AttackMode.CustomDelay)));
    public Setting<Double> attackDelay = new Setting<>("CustomDelay", (double)4, this, 600, 1000).setVisibility(v-> interactionsSubBool.getValue() && attackMode.getValue(AttackMode.CustomDelay));
    public Setting<Boolean> rotationsSubBool = new Setting<>("Rotations", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> rotationsSubBool.getValue());
    public Setting<Bones> bone = new Setting<>("Bone", Bones.Foot, this).setVisibility(v-> rotationsSubBool.getValue());
    public Setting<Boolean> switchSubBool = new Setting<>("Switch", false, this).setMode(Setting.Mode.SUB);
    public Setting<SwitchModes> switchMode = new Setting<>("SwitchMode", SwitchModes.Normal, this).setVisibility(v-> switchSubBool.getValue());
    public Setting<Double> switchDelay = new Setting<>("SwitchDelay", (double)4, this, 0, 1000).setVisibility(v-> switchSubBool.getValue());
    public Setting<Boolean> pauseSubBool = new Setting<>("PauseSubBool", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> eat = new Setting<>("Eat", true, this).setVisibility(v-> pauseSubBool.getValue());
    public Setting<Boolean> mine = new Setting<>("Mine", true, this).setVisibility(v-> pauseSubBool.getValue());
    EntityPlayer target;
    Vec3d toRotate;
    Timer switchTimer = new Timer();
    Timer attackTimer = new Timer();

    @Override
    public void onToggle() {
        super.onToggle();
        switchTimer.reset();
        attackTimer.reset();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(e-> {
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if (needToPause()) return;


        //Aura code
        if (target != null) {
            switch (attackMode.getValue()) {
                case Old:
                    EntityUtil.attackEntity(target,packet.getValue(),swing.getValue());
                    break;
                case New:
                    if (Minecraft.getMinecraft().player.getCooledAttackStrength(0) == 1) {
                        Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, target);
                        Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                        Minecraft.getMinecraft().player.resetCooldown();
                    }
                    break;
                case CustomDelay:
                    if (attackTimer.hasPassedMs(attackDelay.getValue())) {
                        EntityUtil.attackEntity(target,packet.getValue(),swing.getValue());
                        attackTimer.reset();
                    }
                }
            }

        //Switch
        if (switchTimer.hasPassedMs(switchDelay.getValue())) {
            doSwitch();
            switchTimer.reset();
        }

        //Rotations
        if (target != null) {
            switch (bone.getValue()){
                case Foot:
                    toRotate = target.getPositionVector();
                    break;
                case Legs:
                    toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1,target.getPositionVector().z);
                    break;
                case Chestplate:
                    toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1.5,target.getPositionVector().z);
                    break;
                case Helmet:
                    toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1.9,target.getPositionVector().z);
                    break;
            }
        }

        if (rotate.getValue() && target != null && toRotate != null && !needToPause()) {
            Firework.rotationManager.rotateSpoof(toRotate);
        }
    });

    boolean needToPause() {
        if (eat.getValue()  && (mc.player.getHeldItemOffhand().getItem() instanceof ItemFood || mc.player.getHeldItemMainhand().getItem() instanceof ItemFood) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return true;
        } if (mc.playerController.getIsHittingBlock() && mine.getValue()) {
            return true;
        } return false;
    }

    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(e-> {
        if (target == null) return;
        RenderUtils.drawCircle(target.getPositionVector(),1, Color.CYAN,3);
    });



    //Switch
    void doSwitch() {
        switch (switchMode.getValue()) {
            case Normal:
                if (target != null)
                    InventoryUtil.switchToHotbarSlot(ItemSword.class,false);
                break;
            case MultiHand:
                if (target != null)
                    InventoryUtil.doMultiHand(WeaponUtil.theMostPoweredWeapon(target), InventoryUtil.hands.MainHand);
                break;
        }
    }

    public enum Bones{
        Foot, Legs, Chestplate, Helmet
    }

    public enum SwitchModes{
        Normal, MultiHand, None
    }

    public enum AttackMode {
        Old, New, CustomDelay
    }
}
