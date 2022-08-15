package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.ClientTickEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.PredictPlace;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Entity.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.List;
import java.util.Objects;

@ModuleManifest(name = "AutoCrystal", category = Module.Category.COMBAT)
public class AutoCrystal extends Module {
    //Interaction && Sync
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", true, this).setVisibility(v-> interaction.getValue());

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Integer> rotationSpoofs = new Setting<>("RotationSpoofs", 18, this, 0, 20).setVisibility(v-> interaction.getValue() && rotate.getValue());

    //Swing
    public Setting<Boolean> shouldSwing = new Setting<>("Swing", true, this).setVisibility(v-> interaction.getValue());
    public Setting<swing> swingMode = new Setting<>("SwingMode", swing.Both, this).setVisibility(v-> interaction.getValue());
    public enum swing{
        Main, Off, Both, Packet
    }

    //Blow
    public Setting<blow> blowMode = new Setting<>("Blow", blow.Controller, this).setVisibility(v-> interaction.getValue());
    public enum blow{
        Packet, Controller
    }
    public Setting<Boolean> predictFacing = new Setting<>("PredictFacing", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> cancelCrystal = new Setting<>("CancelCrystal", false, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> sync = new Setting<>("Sync", true, this).setVisibility(v-> interaction.getValue());

    //FacePlace / FaceBreak
    public Setting<Boolean> facePlBr = new Setting<>("FacePlace/Break", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> facePlace = new Setting<>("FacePlace", false, this).setVisibility(v-> facePlBr.getValue());
    public Setting<Boolean> faceBreak = new Setting<>("FaceBreak", false, this).setVisibility(v-> facePlBr.getValue());
    public Setting<Integer> targetHealth = new Setting<>("MinTargetHealth", 12, this, 0, 36).setVisibility(v-> facePlBr.getValue() && facePlace.getValue());

    //Ranges
    public Setting<Boolean> ranges = new Setting<>("Ranges", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 5, this, 0, 10).setVisibility(v-> ranges.getValue());
    public Setting<Integer> placeRange = new Setting<>("PlaceRange", 5, this, 0, 6).setVisibility(v-> ranges.getValue());
    public Setting<Integer> placeWallRange = new Setting<>("PlaceWallRange", 5, this, 0, 6).setVisibility(v-> ranges.getValue());
    public Setting<Integer> breakRange = new Setting<>("BreakRange", 5, this, 0, 6).setVisibility(v-> ranges.getValue());
    public Setting<Integer> breakWallRange = new Setting<>("BreakWallRange", 5, this, 0, 6).setVisibility(v-> ranges.getValue());

    //Damages
    public Setting<Boolean> damages = new Setting<>("Damages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 15, this, 0, 36).setVisibility(v-> damages.getValue());
    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 1, this, 0, 36).setVisibility(v-> damages.getValue());

    //Delays
    public Setting<Integer> placeDelay = new Setting<>("PlaceTicks", 5, this, 0, 10);
    public Setting<Integer> breakDelay = new Setting<>("BreakTicks", 10, this, 0, 10);

    //Stuff
    public Setting<Boolean> noSuicide = new Setting<>("NoSuicide", true, this);
    public Setting<Integer> noSuicidePlHealth = new Setting<>("SelfHealth", 10, this, 0, 36).setVisibility(v-> noSuicide.getValue());
    public Setting<Boolean> pauseWhileEating = new Setting<>("PauseWhileEating", true, this);
    public Setting<Boolean> pauseWhileDigging = new Setting<>("PauseWhileDigging", true, this);

    //Render
    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);
    EntityPlayer target;

    BlockPos renderPlacePos;

    int stage = 1;

    int placeTicks;
    int breakTicks;

    //Timers
    Timer renderClear;

    //Rotate stuff
    public Vec3d rotationVec;
    public boolean canRotate;
    public int rSpoofs;

    //Modules
    Module cevBreaker;
    Module aura;

    @Override
    public void onEnable() {
        super.onEnable();
        cevBreaker = Firework.moduleManager.getModuleByClass(CevBreaker.class);
        aura = Firework.moduleManager.getModuleByClass(Aura.class);

        placeTicks = 0;
        breakTicks = 0;

        canRotate = false;

        renderClear = new Timer();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        renderClear = null;

        aura = null;
        cevBreaker = null;
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(render3dE -> {
       if(renderPlacePos == null) return;
       new BlockRenderBuilder(renderPlacePos)
               .addRenderModes(
                       new RenderMode(RenderMode.renderModes.Fill, color.getValue().toRGB())
               ).render();
    });

    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketSoundEffect && sync.getValue()) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            if (packet.getSound().equals(SoundEvents.ENTITY_GENERIC_EXPLODE) && packet.getCategory().equals(SoundCategory.BLOCKS)) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityEnderCrystal) || entity.isDead)
                        continue;
                    if (entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6)
                        entity.setDead();
                }
            }
        }
    });

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketUseEntity && (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(AutoCrystal.mc.world) instanceof EntityEnderCrystal && cancelCrystal.getValue())) {
            Objects.requireNonNull(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world)).setDead();
            mc.world.removeEntityFromWorld(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world).getEntityId());
        }
        if(event.getPacket() instanceof CPacketPlayer && canRotate){
            float rotations[] = RotationUtil.getRotations(rotationVec);
            ((ICPacketPlayer)event.getPacket()).setYaw(rotations[0]);
            ((ICPacketPlayer)event.getPacket()).setPitch(rotations[1]);
            rSpoofs++;
            if(rSpoofs >= rotationSpoofs.getValue()) {
                canRotate = false;
                rSpoofs = 0;
            }
        }
    });

    //Place && Break logic listener
    @Subscribe
    public Listener<ClientTickEvent> logic = new Listener<>(event -> {
        if(fullNullCheck()) return;

        //Target searching
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        //Cleaning render pos
        if(renderClear.hasPassedMs(1000)){
            renderPlacePos = null;
            renderClear.reset();
        }

        if(needToPause())
            return;

        //Placing / Breaking

        EntityEnderCrystal crystal = bestCrystal();

        Pair<BlockPos, Boolean> pos = bestPlacePos();

        BlockPos placePos = pos.one;

        if(placeTicks > 0)
            placeTicks--;

        if(breakTicks > 0)
            breakTicks--;

        if(placeTicks == 0 && stage == 2 && pos.two){
            crystal = CrystalUtils.getCrystalAtPos(pos.one);
            stage = 1;
            breakTicks = 0;
        }

        switch (stage){
            case 1:
                if(breakTicks == 0) {
                    if (crystal != null && (autoSwitch.getValue() || mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL)) {
                        //Rotates
                        if(rotate.getValue())
                            rotate(crystal.getPositionVector().add(0, crystal.getEyeHeight(), 0));

                        //Blows
                        if(blowMode.getValue(blow.Controller))
                            mc.playerController.attackEntity(mc.player, crystal);
                        else if(blowMode.getValue(blow.Packet))
                            mc.getConnection().sendPacket(new CPacketUseEntity(crystal));

                        //Swing
                        if(shouldSwing.getValue())
                            swing(swingMode.getValue());
                    }
                    stage = 2;
                    placeTicks = placeDelay.getValue();
                }
                break;
            case 2:
                if(placeTicks == 0){
                    if(placePos != null && (!predictFacing.getValue() || BlockUtil.getFacingToClick(placePos) != null)){
                        //Switching
                        boolean flag = false;
                        if (mc.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
                            flag = true;
                            if (!autoSwitch.getValue())
                                return;
                        }
                        if (flag) {
                            final int slot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
                            if (slot == -1) return;
                            mc.player.inventory.currentItem = slot;
                            mc.playerController.updateController();
                        }

                        //Facing
                        EnumFacing facing = EnumFacing.UP;

                        //Hit vec
                        Vec3d hitVec = new Vec3d(placePos.getX() + 0.5, placePos.getY(), placePos.getZ() + 0.5);
                        boolean shouldRotate = true;
                        //RayTrace result
                        if(predictFacing.getValue()) {
                            PredictPlace result = BlockUtil.getFacingToClick(placePos);
                            facing = result.getFacing();
                            rotate(hitVec);
                            shouldRotate = false;
                        }

                        if(shouldRotate)
                            rotate(hitVec);

                        renderPlacePos = placePos;
                        mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, facing, EnumHand.MAIN_HAND, (float) hitVec.x - placePos.getX(), (float) hitVec.y - placePos.getY(), (float) hitVec.z - placePos.getZ()));


                        //Swing
                        if(shouldSwing.getValue())
                            swing(swingMode.getValue());
                    }
                    stage = 1;
                    breakTicks = breakDelay.getValue();
                }
                break;
        }
    });

    public void swing(swing swing) {
        switch (swing) {
            case Main:
                //Swings main hand
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            case Off:
                //Swings offhand
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case Both:
                //Swings both hands
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case Packet:
                //Sends a swing packet
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                break;
        }
    }

    public void rotate(Vec3d vec3d){
        rotationVec = vec3d;
        canRotate = true;
        rSpoofs = 0;
    }

    public EntityEnderCrystal bestCrystal(){
        EntityEnderCrystal crystal = null;
        double maxDamage = 0.5;
        for (int size = this.mc.world.loadedEntityList.size(), i = 0; i < size; ++i) {
            final Entity entity = this.mc.world.loadedEntityList.get(i);
            if (entity instanceof EntityEnderCrystal && isValidCrystal((EntityEnderCrystal) entity)) {
                final float targetDamage = CrystalUtils.calculateDamage(entity.posX, entity.posY, entity.posZ, target);
                if (maxDamage <= targetDamage) {
                    maxDamage = targetDamage;
                    crystal = (EntityEnderCrystal) entity;
                }
            }
        }
        return crystal;
    }

    public Pair<BlockPos, Boolean> bestPlacePos(){
        BlockPos placePos = null;
        boolean replace = false;
        double maxDamage = 0.5;
        final List<BlockPos> sphere = BlockUtil.getSphereRealth(placeRange.getValue(), true);
        for (int size = sphere.size(), i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (isValidBlockPos(pos)) {
                replace = CrystalUtils.getCrystalAtPos(pos) != null;
                final float targetDamage = CrystalUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
                if (maxDamage <= targetDamage) {
                    maxDamage = targetDamage;
                    placePos = pos;
                }
            }
        }
        return new Pair<>(placePos, replace);
    }

    public boolean isValidCrystal(EntityEnderCrystal crystal){

        //IsNull and IsDead check
        if(crystal == null || crystal.isDead)
            return false;

        //Distance check
        if(crystal.getDistance(mc.player) > (mc.player.canEntityBeSeen(crystal) ? breakRange.getValue() : breakWallRange.getValue()))
            return false;

        //Max Self && Min Target check
        if(CrystalUtils.calculateDamage(crystal, mc.player) > maxSelfDmg.getValue() || CrystalUtils.calculateDamage(crystal, target) < minTargetDmg.getValue())
            return false;

        //Face break check
        if(faceBreak.getValue() && (target.getPosition().getY()+1 == crystal.getPosition().getY()) && (target.getHealth() + target.getAbsorptionAmount() > targetHealth.getValue()))
            return false;

        //No suicide check
        if(noSuicide.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() - CrystalUtils.calculateDamage(crystal, mc.player) < noSuicidePlHealth.getValue())
            return false;

        //We passed all check so crystal is valid
        return true;
    }

    public boolean isValidBlockPos(BlockPos pos){
        //IsNull check
        if(pos == null)
            return false;

        //Can place crystal
        if(!canPlaceCrystal(pos))
            return false;

        //Predict face check
        if(predictFacing.getValue() && BlockUtil.getFacingToClick(pos) == null)
            return false;

        //Distance check
        if(mc.player.getDistanceSq(pos) > (PlayerUtil.canSeeBlock(pos) ? placeRange.getValue()*placeRange.getValue() : placeWallRange.getValue()*placeWallRange.getValue()))
            return false;

        //Min self damage && Max target damage
        if(CrystalUtils.calculateDamage(pos, mc.player) > maxSelfDmg.getValue() || CrystalUtils.calculateDamage(pos, target) < minTargetDmg.getValue())
            return false;

        //Face break check
        if(faceBreak.getValue() && (target.getPosition().getY() == pos.getY()) && (target.getHealth() + target.getAbsorptionAmount() > targetHealth.getValue()))
            return false;

        //No suicide check
        if(noSuicide.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() - CrystalUtils.calculateDamage(pos, mc.player) < noSuicidePlHealth.getValue())
            return false;

        //We passed all check so pos is valid
        return true;
    }

    public boolean needToPause(){
        if(pauseWhileEating.getValue()
                && (mc.player.getHeldItemOffhand().getItem() instanceof ItemFood || mc.player.getHeldItemMainhand().getItem() instanceof ItemFood)
                && mc.gameSettings.keyBindUseItem.isKeyDown())
            return true;

        if(pauseWhileDigging.getValue() && mc.playerController.getIsHittingBlock())
            return true;

        if(cevBreaker.isEnabled.getValue() || aura.isEnabled.getValue()
        )
            return true;

        return false;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        Block block = BlockUtil.getBlock(pos);

        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();

            if (floor == Blocks.AIR && ceil == Blocks.AIR) {
                for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0)))) {
                    if(entity instanceof EntityEnderCrystal) continue;
                    if (!entity.isDead) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }
}
