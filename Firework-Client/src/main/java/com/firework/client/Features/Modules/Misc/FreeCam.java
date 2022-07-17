package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Globals;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.WorldUtils;
import net.minecraft.entity.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@ModuleManifest(name = "FreeCam",category = Module.Category.MISC)
public class FreeCam extends Module {
    private double posX;
    private double posY;
    private double posZ;
    private float pitch;
    private float yaw;
    private double startPosX;
    private double startPosY;
    private double startPosZ;
    private float startPitch;
    private float startYaw;
    private boolean isRidingEntity;
    private Entity ridingEntity;

    public Setting<Double> speed = new Setting<>("speed", (double)3, this, 1, 10);
    public Setting<Boolean> esp = new Setting<>("esp", false, this);
    public Setting<Boolean> tracer = new Setting<>("tracer", false, this);

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onRender(RenderWorldLastEvent event) {
        try {
            double renderPosX = Globals.MC.getRenderManager().viewerPosX;
            double renderPosY = Globals.MC.getRenderManager().viewerPosY;
            double renderPosZ = Globals.MC.getRenderManager().viewerPosZ;
            if (esp.getValue()) {
                RenderUtils.drawFreecamESP(this.posX - 0.3, this.posY, this.posZ - 0.3, 255, 255, 255);
            }
            if (tracer.getValue()) {
                try {
                    GL11.glPushMatrix();
                    GL11.glEnable((int)2848);
                    GL11.glDisable((int)2929);
                    GL11.glDisable((int)3553);
                    GL11.glDepthMask((boolean)false);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glEnable((int)3042);
                    GL11.glLineWidth((float)2.0f);
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians(mc.player.rotationPitch))).rotateYaw(-((float)Math.toRadians(mc.player.rotationYaw)));
                    GL11.glBegin((int)2);
                    GL11.glVertex3d((double)eyes.x, (double)((double)mc.player.getEyeHeight() + eyes.y), (double)eyes.z);
                    GL11.glVertex3d((double)(this.posX - renderPosX), (double)(this.posY - renderPosY + 1.0), (double)(this.posZ - renderPosZ));
                    GL11.glEnd();
                    GL11.glDisable((int)3042);
                    GL11.glDepthMask((boolean)true);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)2929);
                    GL11.glDisable((int)2848);
                    GL11.glPopMatrix();
                }
                catch (Exception eyes) {
                    // empty catch block
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Text event) {
        Minecraft MC = FMLClientHandler.instance().getClient();
        ScaledResolution scaledresolution = new ScaledResolution(MC);
        String name = "FREECAM";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Globals.MC.player != null) {
            boolean bl = this.isRidingEntity = Globals.MC.player.getRidingEntity() != null;
            if (Globals.MC.player.getRidingEntity() == null) {
                this.posX = Globals.MC.player.posX;
                this.posY = Globals.MC.player.posY;
                this.posZ = Globals.MC.player.posZ;
            } else {
                this.posX = Globals.MC.player.posX;
                this.posY = Globals.MC.player.posY;
                this.posZ = Globals.MC.player.posZ;
                this.ridingEntity = Globals.MC.player.getRidingEntity();
                Globals.MC.player.dismountRidingEntity();
            }
            this.pitch = Globals.MC.player.rotationPitch;
            this.yaw = Globals.MC.player.rotationYaw;
            Globals.MC.player.capabilities.isFlying = true;
            Globals.MC.player.capabilities.setFlySpeed((float)speed.getValue().floatValue());
            Globals.MC.player.noClip = true;
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.capabilities.isFlying = false;
        WorldUtils.reloadChunks();
        EntityPlayerSP localPlayer = Globals.MC.player;
        if (localPlayer != null) {
            Globals.MC.player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
            this.posZ = 0.0;
            this.posY = 0.0;
            this.posX = 0.0;
            this.yaw = 0.0f;
            this.pitch = 0.0f;
            Globals.MC.player.capabilities.setFlySpeed(0.05f);
            if (this.isRidingEntity) {
                Globals.MC.player.startRiding(this.ridingEntity, true);
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        MovementInput movementInput = Globals.MC.player.movementInput;
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = Globals.MC.player.rotationYaw;
        double move = 0.5 + speed.getValue();
        if (!Globals.MC.gameSettings.keyBindJump.isKeyDown() && !Globals.MC.gameSettings.keyBindSneak.isKeyDown()) {
            Globals.MC.player.motionY = 0.0;
        }
        if (Globals.MC.gameSettings.keyBindJump.isKeyDown()) {
            Globals.MC.player.motionY = 0.5 + speed.getValue();
        }
        if (Globals.MC.gameSettings.keyBindSneak.isKeyDown()) {
            Globals.MC.player.motionY = -(0.5 + speed.getValue());
        }
        if (Globals.MC.gameSettings.keyBindJump.isKeyDown() && Globals.MC.gameSettings.keyBindSneak.isKeyDown()) {
            Globals.MC.player.motionY = 0.0;
        }
        if (forward == 0.0 && strafe == 0.0) {
            Globals.MC.player.motionX = 0.0;
            Globals.MC.player.motionZ = 0.0;
        } else if (!Globals.MC.player.onGround) {
            Globals.MC.player.motionX = forward * move * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * move * Math.sin(Math.toRadians(yaw + 90.0f));
            Globals.MC.player.motionZ = forward * move * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * move * Math.cos(Math.toRadians(yaw + 90.0f));
        }
        Globals.MC.player.capabilities.isFlying = true;
        Globals.MC.player.noClip = true;
        Globals.MC.player.onGround = false;
        Globals.MC.player.fallDistance = 0.0f;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput || event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            this.startPosX = packet.getX();
            this.startPosY = packet.getY();
            this.startPosZ = packet.getZ();
            this.startPitch = packet.getPitch();
            this.startYaw = packet.getYaw();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.posX = this.startPosX;
        this.posY = this.startPosY;
        this.posZ = this.startPosZ;
        this.pitch = this.startPitch;
        this.yaw = this.startYaw;
    }



}
