package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.Test;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.WeaponUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Entity.TargetUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderEntityUtils;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.Random;

@ModuleManifest(
        name = "Aura",
        category = Module.Category.COMBAT
)
public class Aura extends Module {

    public static Setting<Boolean> enabled = null;
    public Aura() {
        enabled = this.isEnabled;
    }

    public Setting<Double> targetRange = new Setting<>("TargetRange", (double)4, this, 0, 6);
    public Setting<Boolean> targetsSubBool = new Setting<>("Targets", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> players = new Setting<>("Players", true, this).setVisibility(v-> targetsSubBool.getValue());
    public Setting<Boolean> animals = new Setting<>("Animals", true, this).setVisibility(v-> targetsSubBool.getValue());
    public Setting<Boolean> mobs = new Setting<>("Mobs", true, this).setVisibility(v-> targetsSubBool.getValue());
    public Setting<Boolean> boat = new Setting<>("Boat", true, this).setVisibility(v-> targetsSubBool.getValue());
    public Setting<Boolean> armourStand = new Setting<>("ArmourStand", true, this).setVisibility(v-> targetsSubBool.getValue());
    public Setting<Boolean> interactionsSubBool = new Setting<>("Interact", false, this).setMode(Setting.Mode.SUB);
    public Setting<AttackMode> attackMode = new Setting<>("Mode", AttackMode.CustomDelay, this).setVisibility(v-> interactionsSubBool.getValue());
    public Setting<Boolean> packet = new Setting<>("Packet", true, this).setVisibility(v-> interactionsSubBool.getValue() && (attackMode.getValue(AttackMode.Old) || attackMode.getValue(AttackMode.CustomDelay)));
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(v-> interactionsSubBool.getValue() && (attackMode.getValue(AttackMode.Old) || attackMode.getValue(AttackMode.CustomDelay)));
    public Setting<Double> attackDelay = new Setting<>("CustomDelay", (double)4, this, 1, 1000).setVisibility(v-> interactionsSubBool.getValue() && attackMode.getValue(AttackMode.CustomDelay));
    public Setting<Boolean> randomAttackDelay = new Setting<>("RandomAttackDelay", true, this).setVisibility(v-> interactionsSubBool.getValue() && attackMode.getValue(AttackMode.CustomDelay));

    public Setting<Boolean> rotationsSubBool = new Setting<>("Rotations", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> rotationsSubBool.getValue());
    public Setting<Bones> bone = new Setting<>("Bone", Bones.Foot, this).setVisibility(v-> rotationsSubBool.getValue());
    public Setting<Double> randomBoneDelay = new Setting<>("RandomDelay", (double)500, this, 1, 1000).setVisibility(v-> rotationsSubBool.getValue() && bone.getValue(Bones.Random));

    public Setting<Boolean> switchSubBool = new Setting<>("Switch", false, this).setMode(Setting.Mode.SUB);
    public Setting<SwitchModes> switchMode = new Setting<>("SwitchMode", SwitchModes.Normal, this).setVisibility(v-> switchSubBool.getValue());
    public Setting<Double> switchDelay = new Setting<>("SwitchDelay", (double)4, this, 0, 1000).setVisibility(v-> switchSubBool.getValue());
    public Setting<Boolean> pauseSubBool = new Setting<>("Pause", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> eat = new Setting<>("Eat", true, this).setVisibility(v-> pauseSubBool.getValue());
    public Setting<Boolean> mine = new Setting<>("Mine", true, this).setVisibility(v-> pauseSubBool.getValue());
    public Setting<Boolean> renderSubBool = new Setting<>("Render", false, this).setMode(Setting.Mode.SUB);
    public Setting<RenderMode> renderMode = new Setting<>("RenderMode", RenderMode.Circle, this).setVisibility(v-> renderSubBool.getValue());
    public Setting<Double> moveCircleDelay = new Setting<>("MoveCircleDelay", (double)3, this, 1, 100).setVisibility(v-> renderSubBool.getValue() && renderMode.getValue(RenderMode.Circle));
    public Setting<HSLColor> circleColor = new Setting<>("CircleColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderSubBool.getValue() && renderMode.getValue(RenderMode.Circle));
    public Setting<Double> radius = new Setting<>("Radius", (double)0.5, this, 0, 3).setVisibility(v-> renderSubBool.getValue() && renderMode.getValue(RenderMode.Circle));
    public Setting<HSLColor> boxColor = new Setting<>("BoxColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderSubBool.getValue() && renderMode.getValue(RenderMode.Box));

    public Setting<Integer> lineWidth = new Setting<>("LineWidth", 3, this, 0, 10).setVisibility(v-> renderSubBool.getValue());

    Entity target;
    Vec3d toRotate;
    Timer switchTimer = new Timer();
    Timer attackTimer = new Timer();
    Timer randomBoneTimer = new Timer();
    Timer randomAttackTimer = new Timer();
    Timer animationCircleTimer = new Timer();
    private final Random random = new Random();
    private final Random attackDelayRandom = new Random();

    @Override
    public void onToggle() {
        super.onToggle();
        switchTimer.reset();
        attackTimer.reset();
        randomBoneTimer.reset();
        randomAttackTimer.reset();
        animationCircleTimer.reset();
        y = 0;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(e-> {
        target = TargetUtil.getClosest(players.getValue(),animals.getValue(),mobs.getValue(),boat.getValue(),armourStand.getValue(),targetRange.getValue());

        if (needToPause()) return;

        if (randomAttackTimer.hasPassedMs(200) && randomAttackDelay.getValue()) {
            int randomNumber = attackDelayRandom.nextInt(1000);
            attackDelay.setValue((double) randomNumber);
            randomAttackTimer.reset();
        }


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
                case Random:
                    if (randomBoneTimer.hasPassedMs(randomBoneDelay.getValue())) {
                    int randomNumber = random.nextInt(5);
                    switch (randomNumber) {
                        case 1: toRotate = target.getPositionVector(); randomBoneTimer.reset(); break;
                        case 2: toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1,target.getPositionVector().z); randomBoneTimer.reset(); break;
                        case 3: toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1.5,target.getPositionVector().z); randomBoneTimer.reset(); break;
                        case 4: toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1.9,target.getPositionVector().z); randomBoneTimer.reset(); break;
                        }
                    }
            }
        }

        if (rotate.getValue() && target != null && toRotate != null && !needToPause()) {
            e.setCancelled(true);
            RotationUtil.packetFacePitchAndYaw(RotationUtil.getRotations(toRotate)[1],RotationUtil.getRotations(toRotate)[0]);
        }
    });

    boolean needToPause() {
        if (eat.getValue()  && (mc.player.getHeldItemOffhand().getItem() instanceof ItemFood || mc.player.getHeldItemMainhand().getItem() instanceof ItemFood) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return true;
        } if (mc.playerController.getIsHittingBlock() && mine.getValue()) {
            return true;
        } return false;
    }


    double y = 0;
    Vec3d animationVector;
    boolean shouldMoveUp = true;
    boolean shuldMoveDown = false;

    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(e-> {
        switch (renderMode.getValue()) {
            case Circle:
                if (target == null) return;
                if (shouldMoveUp) {
                    if (animationCircleTimer.hasPassedMs(moveCircleDelay.getValue())) {
                        y+=0.1;
                        animationCircleTimer.reset();
                    }
                } if (shuldMoveDown) {
                if (animationCircleTimer.hasPassedMs(moveCircleDelay.getValue())) {
                    y-=0.1;
                    animationCircleTimer.reset();
                }
            }

                if (y>=2) {
                    returner1();
                } if (y<=0) {
                returner2();
            }
                animationVector = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+y,target.getPositionVector().z);
                RenderUtils.drawCircle(animationVector,radius.getValue(), circleColor.getValue().toRGB(),lineWidth.getValue());
                break;
            case Box:
                if (target == null) return;
                RenderEntityUtils.drawEntityBox(target,boxColor.getValue().toRGB(),true,boxColor.getValue().alpha,lineWidth.getValue());
                break;
        }
    });

    void returner1() {
        if (shouldMoveUp) {
            shouldMoveUp = false;
            shuldMoveDown = true;
        }
    }
    void returner2() {
        if (shuldMoveDown) {
            shouldMoveUp = true;
            shuldMoveDown = false;
        }
    }

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
        Foot, Legs, Chestplate, Helmet, Random
    }

    public enum SwitchModes{
        Normal, MultiHand, None
    }

    public enum AttackMode {
        Old, New, CustomDelay
    }

    public enum RenderMode{
        Circle, Box
    }
}
