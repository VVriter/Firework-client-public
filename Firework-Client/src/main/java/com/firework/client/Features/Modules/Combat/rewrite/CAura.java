package com.firework.client.Features.Modules.Combat.rewrite;

import com.firework.client.Features.Modules.Combat.Aura;
import com.firework.client.Features.Modules.Combat.CevBreaker;
import com.firework.client.Features.Modules.Combat.crystal.AutoCrystalRewrite;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Math.Inhibitator;
import com.firework.client.Implementations.Utill.Math.Interpolation;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.TickTimer;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.Objects;

@ModuleManifest(
        name = "CAura",
        category = Module.Category.COMBAT,
        description = ""
)

public class CAura extends Module {

    public Setting<Double> targetRange = new Setting<>("TargetRange", (double)4, this, 1, 6);
    public Setting<Integer> attackRange = new Setting<>("AttackRange", 3, this, 1, 6);
    public Setting<Integer> attackWallRange = new Setting<>("AttackWallRange", 3, this, 1, 6);

    public Setting<Hand> hand = new Setting<>("Hand", Hand.Main, this);
    public enum Hand{
        Main, Off
    }



    public Setting<Boolean> delaySubBool = new Setting<>("Delay", false, this).setMode(Setting.Mode.SUB);
    public Setting<DelayMode> delayMode = new Setting<>("DelayMode", DelayMode.Normal, this).setVisibility(()-> delaySubBool.getValue());
    public enum DelayMode{
        Normal, Ticks
    }
    public Setting<Integer> placeDelay= new Setting<>("PlaceDelay", 200, this, 1, 1000).setVisibility(()-> delaySubBool.getValue() && delayMode.getValue(DelayMode.Normal));
    public Setting<Double> breakDelay= new Setting<>("BreakDelay", (double)200, this, 1, 1000).setVisibility(()-> delaySubBool.getValue() && delayMode.getValue(DelayMode.Normal));

    public Setting<Integer> placeDelayTicks = new Setting<>("PlaceTicks", 1, this, 1, 20).setVisibility(()-> delaySubBool.getValue() && delayMode.getValue(DelayMode.Ticks));
    public Setting<Integer> breakDelayTicks = new Setting<>("BreakTicks", 1, this, 1, 20).setVisibility(()-> delaySubBool.getValue() && delayMode.getValue(DelayMode.Ticks));


    public Setting<Boolean> rotationSubBool = new Setting<>("Rotations", false, this).setMode(Setting.Mode.SUB);
    public Setting<RotationMode> rotationMode = new Setting<>("Mode", RotationMode.Instant, this).setVisibility(()-> rotationSubBool.getValue());
    public enum RotationMode{
        Instant, YawStep, None
    }
    public Setting<Boolean> placeSubBool = new Setting<>("Place", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(()-> placeSubBool.getValue());
    public Setting<Boolean> exactHand = new Setting<>("ExactHand", true, this).setVisibility(()-> placeSubBool.getValue());


    public Setting<Boolean> breakSubBool = new Setting<>("Break", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> cancelCrystal = new Setting<>("CancelCrystal", false, this).setVisibility(()-> breakSubBool.getValue());
    public Setting<Boolean> inhibit = new Setting<>("Inhibit", false, this).setVisibility(()-> breakSubBool.getValue());
    public Setting<Double> startVal = new Setting<>("StartVal", (double)200, this, 1, 1000).setVisibility(()-> breakSubBool.getValue() && inhibit.getValue());
    public Setting<Double> endVal = new Setting<>("EndVal", (double)400, this, 1, 1000).setVisibility(()-> breakSubBool.getValue() && inhibit.getValue());
    public Setting<BreakMode> breakMode = new Setting<>("Mode", BreakMode.Controller, this).setVisibility(()-> breakSubBool.getValue());
    public enum BreakMode{
        Controller, Packet
    }




    public Setting<Boolean> swapSubBool = new Setting<>("Swap", false, this).setMode(Setting.Mode.SUB);
    public Setting<SwitchModes> switchMode = new Setting<>("SwitchMode", SwitchModes.Normal, this).setVisibility(()-> swapSubBool.getValue());
    public Setting<Double> switchDelay = new Setting<>("SwitchDelay", (double)200, this, 0, 1000).setVisibility(()-> swapSubBool.getValue());
    public enum SwitchModes{
        Normal, Silent, None
    }




    public Setting<Boolean> pauseSubBool = new Setting<>("Pause", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> pauseWhileEating = new Setting<>("Eating", true, this).setVisibility(()-> pauseSubBool.getValue());
    public Setting<Boolean> pauseWhileDigging = new Setting<>("Digging", true, this).setVisibility(()-> pauseSubBool.getValue());




    public Setting<Boolean> noSuicideSubBool = new Setting<>("NoSuicide", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> EnableNoSuicide = new Setting<>("EnableNoSuicide", true, this).setVisibility(()-> noSuicideSubBool.getValue());
    public Setting<Double> suicideHealth = new Setting<>("Health", (double)3, this, 1, 25).setVisibility(()-> noSuicideSubBool.getValue() && EnableNoSuicide.getValue());
    public Setting<Boolean> damagesSubBool = new Setting<>("Damages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Double> minTargetDmg = new Setting<>("MinTargetDmg", (double)1, this, 1, 26).setVisibility(()-> damagesSubBool.getValue());
    public Setting<Double> maxSelfDmg = new Setting<>("MaxSelfDmg", (double)15, this, 1, 26).setVisibility(()-> damagesSubBool.getValue());

    public Setting<Boolean> antiWeaknessSubBool = new Setting<>("AntiWeakness", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> enableAntiWeakness = new Setting<>("EnableAntiWeakness", false, this).setVisibility(()-> antiWeaknessSubBool.getValue());
    public Setting<WeaknessMode> weaknessMode = new Setting<>("SwithSwordMode", WeaknessMode.Normal, this).setVisibility(()-> antiWeaknessSubBool.getValue());
    public enum WeaknessMode{
        Normal, Silent
    }
    EntityPlayer target;
    BlockPos posToPlace;
    Vec3d posToRotate;

    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();

    Module cevBreaker;
    Module aura;

    Timer switchTimer = new Timer();
    Inhibitator inhibitator = new Inhibitator();

    TickTimer placeTickTimer = new TickTimer();
    TickTimer breakTickTimer = new TickTimer();

    @Override
    public void onEnable() {
        super.onEnable();
        cevBreaker = Firework.moduleManager.getModuleByClass(CevBreaker.class);
        aura = Firework.moduleManager.getModuleByClass(Aura.class);
    }

    @Override
    public void onToggle() {
        super.onToggle();
        placeTimer.reset();
        breakTimer.reset();
        switchTimer.reset();
        placeTickTimer.reset();
        breakTickTimer.reset();
    }

        @Subscribe
        public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(e-> {
            target = PlayerUtil.getClosestTarget(targetRange.getValue());
            if (target == null) return;
            if (needToPause()) return;

            posToPlace = CrystalUtils.bestCrystalPos((EntityPlayer) target, attackRange.getValue(),maxSelfDmg.getValue().floatValue(),minTargetDmg.getValue().floatValue());

            if (posToPlace == null) return;
            posToRotate = new Vec3d(posToPlace.getX(), posToPlace.getY(), posToPlace.getZ());

            if (inhibit.getValue()) {
                inhibitator.doInhibitation(breakDelay,0,endVal.getValue(),startVal.getValue(),5);
            }

            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(posToPlace.add(0, 1, 0)))) {
                if (!entity.isDead && entity instanceof EntityEnderCrystal) {
                    if (isValidCrystal((EntityEnderCrystal) entity)) {
                        switch (delayMode.getValue()) {
                            case Normal:
                                if (breakTimer.hasPassedMs(breakDelay.getValue())) {
                                    switch (breakMode.getValue()) {
                                        case Controller:
                                            //Weakness
                                            if (enableAntiWeakness.getValue()) {
                                                doWeaknessSwitch();
                                                switchTimer.reset();
                                            }
                                            Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, entity);
                                            Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                                            breakTimer.reset();
                                            break;
                                        case Packet:
                                            //Weakness
                                            if (enableAntiWeakness.getValue() && switchTimer.hasPassedMs(switchDelay.getValue())) {
                                                doWeaknessSwitch();
                                                switchTimer.reset();
                                            }
                                            mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                                            breakTimer.reset();
                                            break;
                                    }
                                }
                                break;
                            case Ticks:
                                if (breakTickTimer.hasPassedTicks(breakDelayTicks.getValue())) {
                                    switch (breakMode.getValue()) {
                                        case Controller:
                                            //Weakness
                                            if (enableAntiWeakness.getValue()) {
                                                doWeaknessSwitch();
                                                switchTimer.reset();
                                            }
                                            Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, entity);
                                            Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                                            breakTickTimer.reset();
                                            break;
                                        case Packet:
                                            //Weakness
                                            if (enableAntiWeakness.getValue() && switchTimer.hasPassedMs(switchDelay.getValue())) {
                                                doWeaknessSwitch();
                                                switchTimer.reset();
                                            }
                                            mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                                            breakTickTimer.reset();
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                }
            }

            if (isValidBlockPos(posToPlace) ){
                switch (delayMode.getValue()) {
                    case Normal:
                        if (placeTimer.hasPassedMs(placeDelay.getValue())) {
                            switch (hand.getValue()) {
                                case Main:
                                    //Switch
                                    if (switchTimer.hasPassedMs(switchDelay.getValue())) {
                                        doSwitch();
                                        switchTimer.reset();
                                    }

                                    BlockUtil.placeCrystalOnBlock(posToPlace,EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue());
                                    placeTimer.reset();
                                    break;
                                case Off:
                                    //Switch
                                    if (switchTimer.hasPassedMs(switchDelay.getValue())) {
                                        doSwitch();
                                        switchTimer.reset();
                                    }

                                    BlockUtil.placeCrystalOnBlock(posToPlace,EnumHand.OFF_HAND,swing.getValue(),exactHand.getValue());
                                    placeTimer.reset();
                                    break;
                            }
                        }
                        break;
                    case Ticks:
                        if (placeTickTimer.hasPassedTicks(placeDelayTicks.getValue())) {
                            switch (hand.getValue()) {
                                case Main:
                                    //Switch
                                    if (switchTimer.hasPassedMs(switchDelay.getValue())) {
                                        doSwitch();
                                        switchTimer.reset();
                                    }

                                    BlockUtil.placeCrystalOnBlock(posToPlace,EnumHand.MAIN_HAND,swing.getValue(),exactHand.getValue());
                                    placeTickTimer.reset();
                                    break;
                                case Off:
                                    //Switch
                                    if (switchTimer.hasPassedMs(switchDelay.getValue())) {
                                        doSwitch();
                                        switchTimer.reset();
                                    }
                                    BlockUtil.placeCrystalOnBlock(posToPlace,EnumHand.OFF_HAND,swing.getValue(),exactHand.getValue());
                                    placeTickTimer.reset();
                                    break;
                            }
                        }
                        break;
                }
            }


            switch (rotationMode.getValue()) {
                case None:
                    //Empty block
                    break;
                case Instant:
                    //Rotations
                    float[] rotate = RotationUtil.getRotations(posToRotate);
                    RotationUtil.packetFacePitchAndYaw(rotate[1],rotate[0]);
                    e.setCancelled(true);
                    break;
                case YawStep:
                    //Rotations
                    break;
            }
        });

    //Cancel crystal
    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketUseEntity && (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(AutoCrystalRewrite.mc.world) instanceof EntityEnderCrystal && cancelCrystal.getValue())) {
            Objects.requireNonNull(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world)).setDead();
            mc.world.removeEntityFromWorld(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world).getEntityId());
        }
    });

    private static final ResourceLocation CRYSTAL_LOCATION = new ResourceLocation("firework.FireworkRounded.png");

        @Subscribe
        public Listener<Render3dE> ex = new Listener<>(e-> {
            if (target == null) return;
            if (posToPlace == null) return;
            RenderUtils.drawBoxESP(posToPlace,Color.RED,1,true,true,100,1);
            String text = String.valueOf(target.getHealth());
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            double x = posToPlace.getX() + 0.5;
            double y = posToPlace.getY() + (0.5);
            double z = posToPlace.getZ() + 0.5;

            float scale = 0.016666668f * (1.3f);

            GlStateManager.translate(x - Interpolation.getRenderPosX(),
                    y - Interpolation.getRenderPosY(),
                    z - Interpolation.getRenderPosZ());

            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);

            GlStateManager.rotate(mc.player.rotationPitch,
                    mc.gameSettings.thirdPersonView == 2
                            ? -1.0f
                            : 1.0f,
                    0.0f,
                    0.0f);

            GlStateManager.scale(-scale, -scale, scale);

            int distance = (int) mc.player.getDistance(x, y, z);
            float scaleD = (distance / 2.0f) / (2.0f + (2.0f - 1));
            if (scaleD < 1.0f) {
                scaleD = 1;
            }

            GlStateManager.scale(scaleD, scaleD, scaleD);

            GlStateManager.translate(-(Firework.textManager.getStringWidth(text) / 2.0), 0, 0);

            Firework.textManager.drawString(text, 0, 6.0f,Color.RED.getRGB(),false);
            Minecraft.getMinecraft().getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
            Gui.drawScaledCustomSizeModalRect((int) (Firework.textManager.getStringWidth(text) / 2.0f) - 10, -17, 8, 8, 8, 8, 22, 22, 64, 64);

            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.disablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        });

    public boolean needToPause(){
        if(pauseWhileEating.getValue()
                && (mc.player.getHeldItemOffhand().getItem() instanceof ItemFood || mc.player.getHeldItemMainhand().getItem() instanceof ItemFood)
                && mc.gameSettings.keyBindUseItem.isKeyDown())
            return true;

        if(pauseWhileDigging.getValue() && mc.playerController.getIsHittingBlock())
            return true;

        if(cevBreaker.isEnabled.getValue() || aura.isEnabled.getValue())
            return true;

        return false;
    }


    boolean isValidCrystal(EntityEnderCrystal crystal){

        //IsNull and IsDead check
        if(crystal == null || crystal.isDead)
            return false;

        //Distance check
        if(crystal.getDistance(mc.player) > (mc.player.canEntityBeSeen(crystal) ? attackRange.getValue() : attackWallRange.getValue()))
            return false;

        //Max Self && Min Target check
        if(CrystalUtils.calculateDamage(crystal, mc.player) > maxSelfDmg.getValue() || CrystalUtils.calculateDamage(crystal, target) < minTargetDmg.getValue())
            return false;

        //No suicide check
        if(EnableNoSuicide.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() - CrystalUtils.calculateDamage(crystal, mc.player) < suicideHealth.getValue())
            return false;

        //We passed all check so crystal is valid
        return true;
    }

    boolean isValidBlockPos(BlockPos pos){
        //IsNull check
        if(pos == null)
            return false;

        //Can place crystal
        if(!CrystalUtils.canPlaceCrystal(pos))
            return false;

    /*    //Predict face check
        if(facing.getValue() && BlockUtil.getFacingToClick(pos) == null)
            return false; */

        //Distance check
        if(mc.player.getDistanceSq(pos) > (PlayerUtil.canSeeBlock(pos) ? attackRange.getValue()*attackRange.getValue() : attackWallRange.getValue()*attackWallRange.getValue()))
            return false;

        //Min self damage && Max target damage
        if(CrystalUtils.calculateDamage(pos, mc.player) > maxSelfDmg.getValue() || CrystalUtils.calculateDamage(pos, target) < minTargetDmg.getValue())
            return false;

        //No suicide check
        if(EnableNoSuicide.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() - CrystalUtils.calculateDamage(pos, mc.player) < suicideHealth.getValue())
            return false;

        //We passed all check so pos is valid
        return true;
    }

    void doSwitch() {
        switch (switchMode.getValue()) {
            case Normal:
                if (target != null)
                    InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class,false);
                break;
            case Silent:
                InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class,true);
                break;
        }
    }

    void doWeaknessSwitch() {
            if (mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                switch (weaknessMode.getValue()) {
                    case Normal:
                        InventoryUtil.switchToHotbarSlot(ItemSword.class,false);
                        break;
                    case Silent:
                        InventoryUtil.switchToHotbarSlot(ItemSword.class,true);
                        break;
                }
            }
    }
}
