package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Movement.MoveEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ISPacketPlayerPosLook;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Client.TimeVec3d;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ModuleManifest(name = "PacketFly",category = Module.Category.MISCELLANEOUS)
public class PacketFly extends Module {

    /*******************************Settings************************************************/

    public Setting<Enum> type = new Setting<>("type", Type.FAST, this);
    public enum Type{
        FACTOR, SETBACK, FAST, SLOW, DESYNC
    }

    public Setting<Enum> packetMode = new Setting<>("packetMode", Mode.UP, this);
    public enum Mode{
        UP, PRESERVE, DOWN, LIMITJITTER, BYPASS, OBSCURE
    }

    public Setting<Boolean> strict = new Setting<>("strict", true, this);
    public Setting<Boolean> bounds = new Setting<>("bounds", true, this);

    public Setting<Enum> phase = new Setting<>("phase", Phase.NCP, this);
    public enum Phase{
        NONE, VANILLA, NCP
    }

    public Setting<Boolean> multiAxis = new Setting<>("multiAxis", false, this);
    public Setting<Boolean> noPhaseSlow = new Setting<>("noPhaseSlow", false, this);

    public Setting<Double> speed = new Setting<>("speed", (double)0.3, this, 0, 1);

    public Setting<Double> factor = new Setting<>("factor", (double)1, this, 0, 10).setVisibility(v-> type.getValue(Type.FACTOR));

    public Setting<Enum> antiKickMode = new Setting<>("antiKickMode", AntiKick.NORMAL, this);
    public enum AntiKick{
        NONE, NORMAL, LIMITED, STRICT
    }

    public Setting<Enum> limit = new Setting<>("limit", Limit.NONE, this);
    public enum Limit{
        NONE, STRONG, STRICT
    }

    public Setting<Boolean> constrict = new Setting<>("constrict", false, this);
    public Setting<Boolean> jitter = new Setting<>("jitter", false, this);
    public Setting<Boolean> boost = new Setting<>("boost", false, this);

    public Setting<Integer> key1 = new Setting<>("Snap", Keyboard.KEY_NONE, this).setVisibility(v-> type.getValue(Type.FACTOR));
    public Setting<Double> motion = new Setting<>("Distance", (double)0.3, this, 0, 20).setVisibility(v-> type.getValue(Type.FACTOR));










    private int teleportId;

    private CPacketPlayer.Position startingOutOfBoundsPos;

    private ArrayList<CPacketPlayer> packets = new ArrayList<>();
    private Map<Integer, TimeVec3d> posLooks = new ConcurrentHashMap<>();

    private int antiKickTicks = 0;
    private int vDelay = 0;
    private int hDelay = 0;

    private boolean limitStrict = false;
    private int limitTicks = 0;
    private int jitterTicks = 0;

    private boolean oddJitter = false;

    double speedX = 0;
    double speedY = 0;
    double speedZ = 0;

    private float postYaw = -400F;
    private float postPitch = -400F;

    private int factorCounter = 0;

    Timer intervalTimer = new Timer();

    private static final Random random = new Random();


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        // Prevents getting kicked from messing up your game
        if (mc.currentScreen instanceof GuiDisconnected || mc.currentScreen instanceof GuiMainMenu || mc.currentScreen instanceof GuiMultiplayer ||
                mc.currentScreen instanceof GuiDownloadTerrain) {
            this.toggle();
        }

        //Retard check
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }

        if (mc.player.ticksExisted % 20 == 0) {
            cleanPosLooks();
        }

        mc.player.setVelocity(0.0D, 0.0D, 0.0D);

        if (teleportId <= 0 && type.getValue() != Type.SETBACK) {
            // sending this without any other packets will probs cause server to send SPacketPlayerPosLook to fix our pos
            startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1, randomHorizontal(), mc.player.onGround);
            packets.add(startingOutOfBoundsPos);
            mc.player.connection.sendPacket(startingOutOfBoundsPos);
            return;
        }

        boolean phasing = checkCollisionBox();

        speedX = 0;
        speedY = 0;
        speedZ = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown() && (hDelay < 1 || (multiAxis.getValue() && phasing))) {
            if (mc.player.ticksExisted % (type.getValue() == Type.SETBACK || type.getValue() == Type.SLOW || limit.getValue() == Limit.STRICT ? 10 : 20) == 0) {
                speedY = (antiKickMode.getValue() != AntiKick.NONE) ? -0.032 : 0.062;
            } else {
                speedY = 0.062;
            }
            antiKickTicks = 0;
            vDelay = 5;
        } else if (mc.gameSettings.keyBindSneak.isKeyDown() && (hDelay < 1 || (multiAxis.getValue() && phasing))) {
            speedY = -0.062;
            antiKickTicks = 0;
            vDelay = 5;
        }

        if ((multiAxis.getValue() && phasing) || !(mc.gameSettings.keyBindSneak.isKeyDown() && mc.gameSettings.keyBindJump.isKeyDown())) {
            if (PlayerUtil.isPlayerMoving()) {
                double[] dir = MathUtil.directionSpeed((phasing && phase.getValue() == Phase.NCP ? (noPhaseSlow.getValue() ? (multiAxis.getValue() ? 0.0465 : 0.062) : 0.031) : 0.26) * speed.getValue());
                if ((dir[0] != 0 || dir[1] != 0) && (vDelay < 1 || (multiAxis.getValue() && phasing))) {
                    speedX = dir[0];
                    speedZ = dir[1];
                    hDelay = 5;
                }
            }
            // WE CANNOT DO ANTIKICK AFTER FLYING UP OR DOWN!!! THIS CAN MESS UP SO MUCH STUFF
            if (antiKickMode.getValue() != AntiKick.NONE && (limit.getValue() == Limit.NONE || limitTicks != 0)) {
                if (antiKickTicks < (packetMode.getValue() == Mode.BYPASS && !bounds.getValue() ? 1 : 3)) {
                    antiKickTicks++;
                } else {
                    antiKickTicks = 0;
                    if (antiKickMode.getValue() != AntiKick.LIMITED || !phasing) {
                        speedY = antiKickMode.getValue() == AntiKick.STRICT ? -0.08 : -0.04;
                    }
                }
            }
        }

        if (phasing) {
            if (phase.getValue() == Phase.NCP && (double) mc.player.moveForward != 0.0 || (double) mc.player.moveStrafing != 0.0 && speedY != 0) {
                speedY /= 2.5;
            }
        }

        if (limit.getValue() != Limit.NONE) {
            if (limitTicks == 0) {
                speedX = 0;
                speedY = 0;
                speedZ = 0;
            } else if (limitTicks == 2 && jitter.getValue()) {
                if (oddJitter) {
                    speedX = 0;
                    speedY = 0;
                    speedZ = 0;
                }
                oddJitter = !oddJitter;
            }
        } else if (jitter.getValue() && jitterTicks == 7) {
            speedX = 0;
            speedY = 0;
            speedZ = 0;
        }

        if (type.getValue(Type.FAST)) {
            mc.player.setVelocity(speedX, speedY, speedZ);
            sendPackets(speedX, speedY, speedZ, (Mode) packetMode.getValue(), true, false);
        }
        if (type.getValue(Type.SLOW)) {
            sendPackets(speedX, speedY, speedZ, (Mode) packetMode.getValue(), true, false);
        }
        if (type.getValue(Type.SETBACK)) {
            mc.player.setVelocity(speedX, speedY, speedZ);
            sendPackets(speedX, speedY, speedZ, (Mode) packetMode.getValue(), false, false);
        }
        if (type.getValue(Type.FACTOR)) {

        }
        if (type.getValue(Type.DESYNC)) {
            float rawFactor = factor.getValue().floatValue();
            if (Keyboard.isKeyDown(key1.getValue()) && intervalTimer.hasPassedMs(3500)) {
                intervalTimer.reset();
                rawFactor = motion.getValue().floatValue();
            }
            int factorInt = (int) Math.floor(rawFactor);
            factorCounter++;
            if (factorCounter > (int) (20D / ((rawFactor - (double) factorInt) * 20D))) {
                factorInt += 1;
                factorCounter = 0;
            }
            for (int i = 1; i <= factorInt; ++i) {
                mc.player.setVelocity(speedX * i, speedY * i, speedZ * i);
                sendPackets(speedX * i, speedY * i, speedZ * i, (Mode)packetMode.getValue(), true, false);
            }
            speedX = mc.player.motionX;
            speedY = mc.player.motionY;
            speedZ = mc.player.motionZ;
        }


        vDelay--;
        hDelay--;

        if (constrict.getValue() && (limit.getValue() == Limit.NONE || limitTicks > 1)) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
        }

        limitTicks++;
        jitterTicks++;

        if (limitTicks > ((limit.getValue() == Limit.STRICT) ? (limitStrict ? 1 : 2) : 3)) {
            limitTicks = 0;
            limitStrict = !limitStrict;
        }

        if (jitterTicks > 7) {
            jitterTicks = 0;
        }
    });

    private void sendPackets(double x, double y, double z, Mode mode, boolean sendConfirmTeleport, boolean sendExtraCT) {
        Vec3d nextPos = new Vec3d(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
        Vec3d bounds = getBoundsVec(x, y, z, mode);

        CPacketPlayer nextPosPacket = new CPacketPlayer.Position(nextPos.x, nextPos.y, nextPos.z, mc.player.onGround);
        packets.add(nextPosPacket);
        mc.player.connection.sendPacket(nextPosPacket);

        if (limit.getValue() != Limit.NONE && limitTicks == 0) return;

        CPacketPlayer boundsPacket = new CPacketPlayer.Position(bounds.x, bounds.y, bounds.z, mc.player.onGround);
        packets.add(boundsPacket);
        mc.player.connection.sendPacket(boundsPacket);

        if (sendConfirmTeleport) {
            teleportId++;

            if (sendExtraCT) {
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId - 1));
            }

            mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId));

            posLooks.put(teleportId, new TimeVec3d(nextPos.x, nextPos.y, nextPos.z, System.currentTimeMillis()));

            if (sendExtraCT) {
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId + 1));
            }
        }

        /*
        if (type.getValue() != Type.FACTOR && type.getValue() != Type.NOJITTER && packetMode.getValue() != Mode.BYPASS) {
            CPacketPlayer currentPos = new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false);
            packets.add(currentPos);
            mc.player.connection.sendPacket(currentPos);
        }
         */
    }

    private Vec3d getBoundsVec(double x, double y, double z, Mode mode) {
        switch (mode) {
            case UP:
                return new Vec3d(mc.player.posX + x, bounds.getValue() ? (strict.getValue() ? 255 : 256) : mc.player.posY + 420, mc.player.posZ + z);
            case PRESERVE:
                return new Vec3d(bounds.getValue() ? mc.player.posX + randomHorizontal() : randomHorizontal(), strict.getValue() ? (Math.max(mc.player.posY, 2D)) : mc.player.posY, bounds.getValue() ? mc.player.posZ + randomHorizontal() : randomHorizontal());
            case LIMITJITTER:
                return new Vec3d(mc.player.posX + (strict.getValue() ? x : randomLimitedHorizontal()), mc.player.posY + randomLimitedVertical(), mc.player.posZ + (strict.getValue() ? z : randomLimitedHorizontal()));
            case BYPASS:
                if (bounds.getValue()) {
                    double rawY = y * 510;
                    return new Vec3d(mc.player.posX + x, mc.player.posY + ((rawY > ((mc.player.dimension == -1) ? 127 : 255)) ? -rawY : (rawY < 1) ? -rawY : rawY), mc.player.posZ + z);
                } else {
                    return new Vec3d(mc.player.posX + (x == 0D ? (random.nextBoolean() ? -10 : 10) : x * 38), mc.player.posY + y, mc.player.posX + (z == 0D ? (random.nextBoolean() ? -10 : 10) : z * 38));
                }
            case OBSCURE:
                return new Vec3d(mc.player.posX + randomHorizontal(), Math.max(1.5D, Math.min(mc.player.posY + y, 253.5D)), mc.player.posZ + randomHorizontal());
            default:
                return new Vec3d(mc.player.posX + x, bounds.getValue() ? (strict.getValue() ? 1 : 0) : mc.player.posY - 1337, mc.player.posZ + z);
        }
    }

    public  double randomHorizontal() {
        int randomValue = random.nextInt(bounds.getValue() ? 80 : (packetMode.getValue() == Mode.OBSCURE ? (mc.player.ticksExisted % 2 == 0 ? 480 : 100) : 29000000)) + (bounds.getValue() ? 5 : 500);
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    public static double randomLimitedVertical() {
        int randomValue = random.nextInt(22);
        randomValue += 70;
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    public static double randomLimitedHorizontal() {
        int randomValue = random.nextInt(10);
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    private void cleanPosLooks() {
        posLooks.forEach((tp, timeVec3d) -> {
            if (System.currentTimeMillis() - timeVec3d.getTime() > TimeUnit.SECONDS.toMillis(30L)) {
                posLooks.remove(tp);
            }
        });
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        packets.clear();
        posLooks.clear();
        teleportId = 0;
        vDelay = 0;
        hDelay = 0;
        postYaw = -400F;
        postPitch = -400F;
        antiKickTicks = 0;
        limitTicks = 0;
        jitterTicks = 0;
        speedX = 0;
        speedY = 0;
        speedZ = 0;
        oddJitter = false;
        startingOutOfBoundsPos = null;
        startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1, randomHorizontal(), mc.player.onGround);
        packets.add(startingOutOfBoundsPos);
        mc.player.connection.sendPacket(startingOutOfBoundsPos);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.setVelocity(0, 0, 0);
        }
       // KonasGlobals.INSTANCE.timerManager.resetTimer(this);
    }

    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (!(mc.currentScreen instanceof GuiDownloadTerrain)) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                if (mc.player.isEntityAlive()) {
                    if (this.teleportId <= 0) {
                        this.teleportId = ((SPacketPlayerPosLook) event.getPacket()).getTeleportId();
                    } else {
                        if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), false) &&
                                type.getValue() != Type.SETBACK) {
                            if (type.getValue() == Type.DESYNC) {
                                posLooks.remove(packet.getTeleportId());
                                event.setCancelled(true);
                                if (type.getValue() == Type.SLOW) {
                                    mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
                                }
                                return;
                            } else if (posLooks.containsKey(packet.getTeleportId())) {
                                TimeVec3d vec = posLooks.get(packet.getTeleportId());
                                if (vec.x == packet.getX() && vec.y == packet.getY() && vec.z == packet.getZ()) {
                                    posLooks.remove(packet.getTeleportId());
                                    event.setCancelled(true);
                                    if (type.getValue() == Type.SLOW) {
                                        mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
                ((ISPacketPlayerPosLook) packet).setYaw(mc.player.rotationYaw);
                ((ISPacketPlayerPosLook) packet).setPitch(mc.player.rotationPitch);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
                teleportId = packet.getTeleportId();
            } else {
                teleportId = 0;
            }
        }
    });

    @SubscribeEvent
    public void onPlayerMove(MoveEvent event) {
        if (type.getValue() != Type.SETBACK && teleportId <= 0) {
            return;
        }

        if (type.getValue() != Type.SLOW) {
            event.setX(speedX);
            event.setY(speedY);
            event.setZ(speedZ);
        }

        if (phase.getValue() != Phase.NONE && phase.getValue() == Phase.VANILLA || checkCollisionBox()) {
            mc.player.noClip = true;
        }
    }

    private boolean checkCollisionBox() {
        if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(0.0, 0.0, 0.0)).isEmpty()) {
            return true;
        }
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, 2.0, 0.0).contract(0.0, 1.99, 0.0)).isEmpty();
    }

    @Subscribe
    public Listener<PacketEvent.Send> onSend = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            if (this.packets.contains(packet)) {
                this.packets.remove(packet);
                return;
            }
            event.setCancelled(true);
        }
    });

    @SubscribeEvent
    public void onBlockPushOut(PlayerSPPushOutOfBlocksEvent event) {
        event.setCanceled(true);
    }
}
