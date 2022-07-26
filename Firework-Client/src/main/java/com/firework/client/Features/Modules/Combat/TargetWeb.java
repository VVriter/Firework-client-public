package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static java.lang.Math.*;

@ModuleManifest(name = "TargetWeb", category = Module.Category.COMBAT)
public class TargetWeb extends Module{

    //Target search range
    private Setting<Integer> targetRange = new Setting<>("TargetRange", 5, this, 0, 12);
    //Web place range
    private Setting<Integer> placeRange = new Setting<>("PlaceRange", 3, this, 0, 6);
    //Hand the module will use to place web
    private Setting<hands> hand = new Setting<>("hand", hands.MainHand, this);
    //Should rotate to place
    private Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    //Should use packet rotate spoof
    private Setting<Boolean> packet = new Setting<>("Packet", false, this);
    //Blocks to place queue
    private ArrayList<BlockPos> line;


    //Web predicted pos
    BlockPos webPos;

    public TargetWeb(){
        line = new ArrayList<>();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        //Sets target
        EntityPlayer target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        //Stops process if web wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.WEB)) == -1) {
            MessageUtil.sendError("No web found in the hotbar", -1117);
            return;
        }

        //Predicts web pos to place
        webPos = EntityUtil.getFlooredPos(target);
        //PLACES WEB
        if(webPos != null)
            if(BlockUtil.getBlock(webPos) == Blocks.AIR)
                if(BlockUtil.getDistance(webPos, EntityUtil.getFlooredPos(mc.player)) <= placeRange.getValue())
                    line.add(webPos);

        //Initializes switch values
        int switchBack = -1;
        Item oldItem = null;

        //Sets oldItem
        oldItem = getItemStack(mc.player.inventory.currentItem).getItem();
        //Sets "should switch back" var, returns 0 if should switch back
        switchBack = switchItems(Item.getItemFromBlock(Blocks.WEB), hands.MainHand);

        //Places webs
        for(BlockPos blockPos : line){
            placeBlock(blockPos);
        }

        //Back switch
        if(switchBack == 0) {
            switchItems(oldItem, hands.MainHand);

            switchBack = -1;
            oldItem = null;
        }

        //Delete placed webs from the queue
        for (int i = 0; i < line.size(); i++) {
            if (BlockUtil.getBlock(line.get(i)) != Blocks.AIR)
                line.remove(i);
        }

    });

    //Returns predicted web pos
    public BlockPos getPredictedWebPos(EntityPlayer entity, double scaleFactor){
        BlockPos flooredPos = EntityUtil.getFlooredPos(entity);
        if(!entity.onGround && entity.motionY < 0 && entity.fallDistance >= 1){
            return flooredPos.add(0, -1, 0);
        }else{
            if(entity.motionX != 0 || entity.motionZ != 0){
                double min = min(entity.motionX, entity.motionZ);
                double max = max(entity.motionX, entity.motionZ);

                int rotationFactorX = (entity.getLookVec().x < 0) ? -1 : 1;
                int rotationFactorZ = (entity.getLookVec().z < 0) ? -1 : 1;

                if(min < max/scaleFactor){
                    if(max == entity.motionX)
                        return flooredPos.add(1*rotationFactorX, 0, 0);
                    else if(max == entity.motionZ)
                        return flooredPos.add(0, 0, 1*rotationFactorZ);
                }else{
                    return flooredPos.add(1*rotationFactorX, 0, 1*rotationFactorZ);
                }
            }
        }
        return null;
    }

    //BlockPos to place
    public void placeBlock(final BlockPos blockPos){
        //Gets "enumHand" enum from "hands" enum
        EnumHand enumHand = hand.getValue(InventoryUtil.hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(webPos == null) return;
        new BlockRenderBuilder(webPos)
                .addRenderModes(
                        new RenderMode(RenderMode.renderModes.Fill, Color.red)
                ).render();
    }

    public int switchItems(Item item, InventoryUtil.hands hand){
        if(hand == InventoryUtil.hands.MainHand){
            if(getClickSlot(getItemSlot(item)) !=  getClickSlot(mc.player.inventory.currentItem)){
                if(getHotbarItemSlot(item) != -1){
                    switchHotBarSlot(getHotbarItemSlot(item));
                }else{
                    swapSlots(getItemSlot(item), getHotbarItemSlot(Item.getItemFromBlock(Blocks.AIR)));
                    switchHotBarSlot(getHotbarItemSlot(item));
                }
            }else{
                return -1;
            }
        }
        return 0;
    }

    public void switchHotBarSlot(int slot){
        InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }

    public static void swapSlots(int from, int to){
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(to), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
    }

    public Vec3i getVec3i(Vec3d vec3d){
        return new Vec3i(round(vec3d.x), round(vec3d.y), round(vec3d.z));
    }
}
