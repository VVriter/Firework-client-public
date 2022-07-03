package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Mixins.MixinsList.IKeyBinding;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;

@ModuleManifest(name = "BlockFly", category = Module.Category.MOVEMENT)
public class BlockFly extends Module {

    //Switch mode
    private Setting<switchModes> switchMode = new Setting<>("Switch", switchModes.Silent, this, switchModes.values());
    private enum switchModes{
        Fast, Silent
    }

    //Place args
    private Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    private Setting<Boolean> packet = new Setting<>("Packet", true, this);

    //Should use scaffold mode
    private Setting<Boolean> scaffold = new Setting<>("ScaffoldMode", true, this);
    //Delay for the scaffold place timer
    private Setting<Integer> placeDelaySC = new Setting<>("PlaceDelayMs", 0, this, 0, 200).setVisibility(scaffold, true);

    //Should use tower mode
    private Setting<Boolean> towerMode = new Setting<>("TowerMode", true, this);
    //Delay for the tower mode fly timer
    private Setting<Integer> flyDelayTW = new Setting<>("FlyDelayMs", 194, this, 0, 200).setVisibility(towerMode, true);
    //Delay for the tower mode place timer
    private Setting<Integer> placeDelayTW = new Setting<>("PlaceDelayMs", 0, this, 0, 200).setVisibility(towerMode, true);

    //Should center
    private Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);

    //Center mode
    private Setting<centerModes> centerMode = new Setting<>("CMode", centerModes.Motion, this, centerModes.values()).setVisibility(shouldCenter, true);
    private enum centerModes{
        Motion, TP, NONE
    }

    //Timer that will count fly delay for the tower mode updater
    Timer flyTimerTW = null;
    //Timer that will count place delay for the tower mode updater
    Timer placeTimerTW = null;

    //Timer that will count place delay for the scaffold mode updater
    Timer placeTimerSC = null;

    //Scaffold blocks to place
    private ArrayList<BlockPos> queue = new ArrayList<>();

    //Last placed block
    private BlockPos lastBlockPos = null;



    //----------TOWER MODE----------

    //Fly updater, will make player flying
    private Updater flyUpdaterTowerMode = new Updater(){
        @Override
        public void run() {
            super.run();
            //Returns if player or world didn't initialize
            if (mc.player == null || mc.world == null) return;
            //Returns if conditions aren't met
            if(!canRunTowerMode() || !canRun() || !towerMode.getValue()) return;

            //Moves player to a center
            if (shouldCenter.getValue()) {
                if (centerMode.getValue(centerModes.Motion)) {
                    centerMotion();
                } else if (centerMode.getValue(centerModes.TP)) {
                    centerTeleport();
                }
            }
            //If fly timer has passed fly delay
            if (flyTimerTW.hasPassedMs(flyDelayTW.getValue())) {
                if (lastBlockPos != null)
                    if (EntityUtil.getFlooredPos(mc.player).getY() - lastBlockPos.getY() > 2)
                        return;
                //Makes player flying
                mc.player.motionY = 0.2444441;
                //Resets fly timer
                flyTimerTW.reset();
            }
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    };

    //Place updater, will place blocks under player in timings to bypass fly
    private Updater placeUpdaterTowerMode = new Updater(){
        @Override
        public void run() {
            super.run();
            //Returns if player or world didn't initialize
            if(mc.player == null || mc.world == null) return;
            //Returns if conditions aren't met
            if(!canRunTowerMode() || !canRun() || !towerMode.getValue()) return;
;
            //If place timer has passed place delay
            if(placeTimerTW.hasPassedMs(placeDelayTW.getValue())) {
                //Places block under the player
                if (BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0)) == Blocks.AIR) {
                    lastBlockPos = EntityUtil.getFlooredPos(mc.player).add(0, -1, 0);
                    mc.player.motionY = 0;
                    placeBlock(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0));
                }
                //Resets place timer
                placeTimerTW.reset();
            }
        }
    };

    //----------SCAFFOLD MODE----------

    private Updater placeUpdaterScaffoldMode = new Updater(){
        @Override
        public void run() {
            super.run();
            //Returns if player or world didn't initialize
            if(mc.player == null || mc.world == null) return;
            //Returns if tower mode is processing
            if((canRunTowerMode() && towerMode.getValue()) || !scaffold.getValue()) return;

            //Placed blocks list
            ArrayList<BlockPos> placedBlocks = new ArrayList<>();

            //Places blocks from queue
            for(BlockPos blockPos : queue){
                if(placeTimerSC.hasPassedMs(placeDelaySC.getValue())){
                    placeBlock(blockPos);
                    placedBlocks.add(blockPos);
                    System.out.println(blockPos);
                    placeTimerSC.reset();
                }
            }

            //Removes placed blocks from queue
            for(BlockPos blockPos : placedBlocks){
                queue.remove(queue.indexOf(blockPos));
            }

            BlockPos blockToPlace = EntityUtil.getFlooredPos(mc.player).add(0, -1, 0);
            if(!mc.player.isSprinting()){
                if(BlockUtil.getBlock(blockToPlace) == Blocks.AIR) {
                    if (!BlockUtil.getPossibleSides(blockToPlace).isEmpty()) {
                        queue.add(blockToPlace);
                    } else {
                        for (BlockPos neighbor : getNeighbors(blockToPlace)) {
                            if (!BlockUtil.getPossibleSides(neighbor).isEmpty()) {
                                queue.add(neighbor);
                                queue.add(blockToPlace);
                                break;
                            }
                        }
                    }
                }
            }else{
                if(BlockUtil.getBlock(blockToPlace) == Blocks.AIR) {
                    queue.add(blockToPlace);
                    queue.add(blockToPlace.offset(mc.player.getHorizontalFacing()));
                }
            }
        }
    };

    @Override
    public void onEnable() {
        super.onEnable();
        //Initializes and resets tower mode place timer
        placeTimerTW = new Timer();
        placeTimerTW.reset();
        //Initializes and resets tower mode fly timer
        flyTimerTW = new Timer();
        flyTimerTW.reset();

        //Initializes and resets scaffold mode place timer
        placeTimerSC = new Timer();
        placeTimerSC.reset();

        //Resets last block pos
        lastBlockPos = null;
        //Registers tower mode place updater
        Firework.updaterManager.registerUpdater(placeUpdaterTowerMode);
        //Registers tower mode fly updater
        Firework.updaterManager.registerUpdater(flyUpdaterTowerMode);

        //Resets place queue
        queue.clear();
        //Registers scaffold mode place updater
        Firework.updaterManager.registerUpdater(placeUpdaterScaffoldMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        //Unregisters tower mode fly updater
        Firework.updaterManager.removeUpdater(flyUpdaterTowerMode);
        //Unregisters tower mode place updater
        Firework.updaterManager.removeUpdater(placeUpdaterTowerMode);

        //Unregisters scaffold mode place updater
        Firework.updaterManager.removeUpdater(placeUpdaterScaffoldMode);
    }

    private boolean canRunTowerMode(){
        //Doesn't allow updaters to work if player isn't pressing jump key
        if(!((IKeyBinding)mc.gameSettings.keyBindJump).pressed())
            return false;

        return true;
    }

    private boolean canRun(){
        //Stops process if obby wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            return false;
        }
        return true;
    }

    //Moves player to a center
    public void centerMotion() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
    }

    //Teleports player to a center
    public void centerTeleport() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        mc.player.connection.sendPacket(new CPacketPlayer.Position(centerPos[0], mc.player.posY, centerPos[2], mc.player.onGround));
        mc.player.setPosition(centerPos[0], mc.player.posY, centerPos[2]);
    }

    //Checks if player was centered
    public boolean isCentered() {
        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
    }

    //BlockPos to place
    private void placeBlock(final BlockPos blockPos){
        //Return if block pos is null
        if(blockPos == null)
            return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(Blocks.OBSIDIAN), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        if(switchMode.getValue(switchModes.Silent)) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //Switches to needed item
    private int switchItems(Item item, InventoryUtil.hands hand){
        if(hand == InventoryUtil.hands.MainHand){
            if(getClickSlot(getItemSlot(item)) !=  getClickSlot(mc.player.inventory.currentItem)){
                int prevSlot = mc.player.inventory.currentItem;
                if (getHotbarItemSlot(item) != -1) {
                    switchHotBarSlot(getHotbarItemSlot(item));
                } else {
                    swapSlots(getItemSlot(item), getHotbarItemSlot(Item.getItemFromBlock(Blocks.AIR)));
                    switchHotBarSlot(getHotbarItemSlot(item));
                }
                return prevSlot;
            }else{
                return -1;
            }
        }
        return 0;
    }

    //Switches hotbar active slot
    private void switchHotBarSlot(int slot){
        InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }

    //Swaps items between 2 slots
    private void swapSlots(int from, int to){
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(to), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
    }

    //Block neighbors
    public BlockPos[] getNeighbors(BlockPos p) {
        return new BlockPos[]{p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
    }
}