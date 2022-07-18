package com.firework.client.Features.Modules.Combat.Rewrite.Ca;

import com.firework.client.Features.Modules.Combat.Rewrite.Ca.PASSSTE.*;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketUseEntity;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ModuleManifest(name = "AutoRerRewrite",category = Module.Category.COMBAT)
public class AutoRerRewrite extends Module {
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 5, this, 1, 10);

    public Setting<Enum> page = new Setting<>("Page", pages.Place, this, pages.values());
    public enum pages{
        Place, Break, Damage, ElseShit, Render
    }
    //Place
    public Setting<Integer> placeRange = new Setting<>("PlaceRange", 5, this, 1, 10).setVisibility(v-> page.getValue(pages.Place));
    public Setting<Integer> placeDelay = new Setting<>("PlaceDelay", 5, this, 0, 200).setVisibility(v-> page.getValue(pages.Place));
    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this, ItemUser.switchModes.values()).setVisibility(v-> page.getValue(pages.Place));
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> page.getValue(pages.Place));

    //Break
    public Setting<Integer> breakRange = new Setting<>("BreakRange", 5, this, 1, 10).setVisibility(v-> page.getValue(pages.Break));
    public Setting<Integer> breakDelay = new Setting<>("BreakDelay", 5, this, 0, 200).setVisibility(v-> page.getValue(pages.Break));
    public Setting<Integer> breakWallRange = new Setting<>("BreakWallRange", 5, this, 1, 10).setVisibility(v-> page.getValue(pages.Break));

    //Damages
    public Setting<Integer> minDamage = new Setting<>("MinDamage", 1, this, 0, 30).setVisibility(v-> page.getValue(pages.Damage));
    public Setting<Integer> maxSelf = new Setting<>("MaxSelf", 20, this, 1, 20).setVisibility(v-> page.getValue(pages.Damage));
    public Setting<Integer> lethalMult = new Setting<>("LethalMult", 0, this, 0, 6).setVisibility(v-> page.getValue(pages.Damage));



    public Setting<Integer> armorScale = new Setting<>("armorScale", 100, this, 0, 100).setVisibility(v-> page.getValue(pages.ElseShit));

    public Setting<HSLColor> color = new Setting<>("color", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.ElseShit));



    ItemUser placer;
    private final List<BlockPos> placedList;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer renderTimer;
    private int rotationPacketsSpoofed;
    public static EntityPlayer currentTarget;
    private BlockPos renderPos;
    private BlockPos placePos;
    private boolean offHand;
    public boolean rotating;
    private boolean offhand;


    public AutoRerRewrite() {
        this.placedList = new ArrayList<BlockPos>();
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.renderTimer = new Timer();
        this.rotationPacketsSpoofed = 0;
        this.renderPos = null;
        this.placePos = null;
        this.offHand = false;
        this.rotating = false;
    }

    @Override
    public void onToggle() {
        super.onToggle();
        this.placedList.clear();
        this.breakTimer.reset();
        this.placeTimer.reset();
        this.renderTimer.reset();
        this.currentTarget = null;
        this.renderPos = null;
        this.offhand = false;
        this.rotating = false;
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.renderTimer.hasPassedMs(500L)) {
            this.placedList.clear();
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.offhand = ((this.mc.player.inventory.offHandInventory.get(0)).getItem() == Items.END_CRYSTAL);
        this.currentTarget = PlayerUtil.getClosestTarget(this.targetRange.getValue());
        if (this.currentTarget == null) {
            return;
        }
        this.doPlace();
        if (event.phase == TickEvent.Phase.START) {
            this.doBreak();
        }
    }


    private void doBreak() {
        EntityHusk pl = new EntityHusk(mc.world);
        pl.setPosition(MovementUtil.extrapolatePlayerPositionWithGravity(mc.player, 40).x, MovementUtil.extrapolatePlayerPositionWithGravity(mc.player, 40).y, MovementUtil.extrapolatePlayerPositionWithGravity(mc.player, 40).z);
        Entity crystal = null;
        double maxDamage = 0.5;
        for (int size = this.mc.world.loadedEntityList.size(), i = 0; i < size; ++i) {
            final Entity entity = this.mc.world.loadedEntityList.get(i);
            if (entity instanceof EntityEnderCrystal && this.mc.player.getDistance(entity) < (this.mc.player.canEntityBeSeen(entity) ? this.breakRange.getValue() : this.breakWallRange.getValue())) {
                final float targetDamage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase) this.currentTarget);
                if (targetDamage > this.minDamage.getValue() || targetDamage * this.lethalMult.getValue() > this.currentTarget.getHealth() + this.currentTarget.getAbsorptionAmount() || ItemUtil.isArmorUnderPercent(this.currentTarget, this.armorScale.getValue())) {
                    final float selfDamage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase) this.mc.player);
                    if (selfDamage <= this.maxSelf.getValue() && selfDamage + 2.0f <= this.mc.player.getHealth() + this.mc.player.getAbsorptionAmount() && selfDamage < targetDamage) {

                            if (maxDamage <= targetDamage) {
                                maxDamage = targetDamage;
                                crystal = entity;
                        }
                    }
                }
            }
        }
        if (crystal != null && this.breakTimer.hasPassedMs(this.breakDelay.getValue())) {
            this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(crystal));
          this.mc.player.swingArm(EnumHand.MAIN_HAND);
            this.breakTimer.reset();
        }
    }

    boolean bebro = false;
    private void doPlace() {
        EntityHusk pl = new EntityHusk(mc.world);
        pl.setPosition(MovementUtil.extrapolatePlayerPositionWithGravity(mc.player, 40).x, MovementUtil.extrapolatePlayerPositionWithGravity(mc.player, 40).y, MovementUtil.extrapolatePlayerPositionWithGravity(mc.player, 40).z);
        BlockPos placePos = null;
        double maxDamage = 0.5;
        final List<BlockPos> sphere = BlockUtil.getSphereRealth(this.placeRange.getValue(), true);
        for (int size = sphere.size(), i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (BlockUtil.canPlaceCrystal(pos, bebro)) {
                final float targetDamage = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase) this.currentTarget);
                if (targetDamage > this.minDamage.getValue() || targetDamage * this.lethalMult.getValue() > this.currentTarget.getHealth() + this.currentTarget.getAbsorptionAmount() || ItemUtil.isArmorUnderPercent(this.currentTarget, this.armorScale.getValue())) {
                    final float selfDamage = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase) this.mc.player);
                    if (selfDamage <= this.maxSelf.getValue() && selfDamage + 2.0f <= this.mc.player.getHealth() + this.mc.player.getAbsorptionAmount() && selfDamage < targetDamage) {

                            if (maxDamage <= targetDamage) {
                                maxDamage = targetDamage;
                                placePos = pos;
                                this.renderPos = pos;
                               // this.renderDamage = targetDamage;
                        }
                    }
                }
            }
        }
        boolean flag = false;
        if (!this.offhand && this.mc.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
            flag = true;
        }
        if (placePos != null) {
            if (this.placeTimer.hasPassedMs(this.placeDelay.getValue())) {
                this.placedList.add(placePos);
                if (placePos != null) {
                 placer.useItem(Items.END_CRYSTAL,placePos, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                }
                this.placeTimer.reset();
            }
            this.renderPos = placePos;
        }
        for (final BlockPos pos2 : BlockUtil.possiblePlacePositions(this.placeRange.getValue())) {
        /*    if (!BlockUtil.rayTracePlaceCheck(pos2, (this.raytrace.getValue(Raytrace.Place) || this.raytrace.getValue() == Raytrace.Both) && mc.player.getDistanceSq(pos2) > MathUtil.square(this.placetrace.getValue()), 1.0f)) {
                continue;
            } */
        }
    }


    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 51 && this.placedList.contains(new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ()))) {
                final ICPacketUseEntity use = (ICPacketUseEntity)new CPacketUseEntity();
                use.setEntityId(packet.getEntityID());
                use.setAction(CPacketUseEntity.Action.ATTACK);
                this.mc.getConnection().sendPacket((Packet)use);
                this.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.breakTimer.reset();
                return;
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet2 = (SPacketSoundEffect) event.getPacket();
            if (packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                final SPacketSoundEffect sPacketSoundEffect = new SPacketSoundEffect();
                new ArrayList(this.mc.world.loadedEntityList).forEach(e -> {
                    if (e instanceof EntityEnderCrystal && ((EntityEnderCrystal) e).getDistanceSq(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) < 36.0) {
                        ((EntityEnderCrystal) e).setDead();
                    }
                });
            }
        }
    }


    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        BlockPos pos = null;
        CPacketUseEntity packet3 = null;
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = (CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)mc.world) instanceof EntityEnderCrystal) {
            pos = packet3.getEntityFromWorld((World)mc.world).getPosition();
        }
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = (CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)mc.world) instanceof EntityEnderCrystal) {
            final EntityEnderCrystal crystal = (EntityEnderCrystal)packet3.getEntityFromWorld((World)mc.world);
            if (EntityUtil.isCrystalAtFeet(crystal, this.targetRange.getValue()) && pos != null) {
                if (placePos != null) {
                    placer.useItem(Items.END_CRYSTAL,placePos,this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                }
            }
        }
    }








    /*******************************************************RENDER STUFF**********************************************/


    PosRenderer posRenderer;
    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("RenderMode", PosRenderer.renderModes.Box, this, PosRenderer.renderModes.values()).setVisibility(v-> page.getValue(pages.Render));
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("BoxMode", PosRenderer.boxeMode.Normal, this, PosRenderer.boxeMode.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Render));
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Render));
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Render));
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Render));
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Render));



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("OutlineMode", PosRenderer.outlineModes.Normal, this, PosRenderer.outlineModes.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Render));
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && page.getValue(pages.Render));
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && page.getValue(pages.Render));
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && page.getValue(pages.Render));
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && page.getValue(pages.Render));
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && !outlineMode.getValue(PosRenderer.outlineModes.None) && page.getValue(pages.Render));
    public Setting<HSLColor> beaconFillColor = new Setting<>("BeaconFillColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Beacon));
    public Setting<Integer> beaconOutlineWidth = new Setting<>("BeaconOutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Beacon));


    @Override
    public void onEnable() {
        super.onEnable();
        placer = new ItemUser(this,switchMode,rotate);
        posRenderer = new PosRenderer(this,renderMode,boxMode,outlineMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }

    @SubscribeEvent
    public void onrender(RenderWorldLastEvent e) {
        if (renderPos != null) {
            posRenderer.doRender(
                    renderPos,
                    colorOutline.getValue().toRGB(),
                    gradientOutlineColor1.getValue().toRGB(),
                    gradientOutlineColor2.getValue().toRGB(),
                    fillColor.getValue().toRGB(),
                    fillColor1.getValue().toRGB(),
                    fillColor2.getValue().toRGB(),
                    outlineWidth.getValue(),
                    boxHeightNormal.getValue().floatValue(),
                    outlineHeightNormal.getValue().floatValue(),
                    beaconFillColor.getValue().toRGB(),
                    beaconOutlineWidth.getValue()
            );
        }
    }
}
