package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "TargetWeb", category = Module.Category.COMBAT)
public class TargetWeb extends Module {

    public Setting<Double> targetRange = new Setting<>("Range", 4d, this, 0, 6);
    public Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle",true, this);

    public Setting<Integer> delay = new Setting<>("Delay",1, this, 1, 10);

    public Setting<BlockPlacer.switchModes> swicth = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Packet", false, this);

    BlockPlacer blockPlacer;

    int remainingDelay;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) {
            onDisableLog();
            return;
        }
        blockPlacer = new BlockPlacer(this, swicth, rotate, packet);

        remainingDelay = delay.getValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blockPlacer = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(Blocks.WEB)) == -1) {
            MessageUtil.sendError("No web found in the hotbar", -1117);
            onDisable();
            return;
        };

        EntityPlayer target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        remainingDelay--;
        if(remainingDelay != 0) return;
        remainingDelay = delay.getValue();
        int result = 0;
        for(BlockPos pos : poses(target)){
            if(BlockUtil.isAir(pos)){
                blockPlacer.placeBlock(pos, Blocks.WEB, false);
                break;
            }else
                result++;
        }

        if(result == 2 && shouldToggle.getValue())
            onDisableLog();
    });

    public BlockPos[] poses(EntityPlayer target){
        return new BlockPos[]{
                EntityUtil.getFlooredPos(target),
                EntityUtil.getFlooredPos(target).offset(EnumFacing.UP)};
    };
}
