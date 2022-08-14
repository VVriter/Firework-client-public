package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.Movement.Step;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.HoleUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@ModuleManifest(name = "HoleFiller", category = Module.Category.COMBAT)
public class HolleFiller extends Module {

    private Setting<modes> mode = new Setting<>("Mode", modes.Obby, this);
    private enum modes {
        Obby, Web
    }

    private Setting<Integer> placeDelay = new Setting<>("PlaceDelay", 2, this, 0, 100);

    //Interaction
    private Setting<Boolean> interactionPage = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this).setVisibility(v-> interactionPage.getValue());
    private Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interactionPage.getValue());
    private Setting<Boolean> packet = new Setting<>("Packet", false, this).setVisibility(v-> interactionPage.getValue());

    //Customization
    private Setting<Boolean> customizationPage = new Setting<>("Customization", false, this).setMode(Setting.Mode.SUB);

    private Setting<logic> logicMode = new Setting<>("Logic", logic.Both, this).setVisibility(v-> customizationPage.getValue());
    private enum logic{
        Both, Double, Single
    }

    private Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", true, this).setVisibility(v-> customizationPage.getValue());
    private Setting<Boolean> disableOnJump = new Setting<>("JumpDisable", true, this).setVisibility(v-> customizationPage.getValue());
    private Setting<Boolean> autoBurrow = new Setting<>("AutoBurrow", false, this).setVisibility(v-> customizationPage.getValue());

    private Setting<Integer> radius = new Setting<>("Radius", 5, this, 0, 10);
    private Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);


    BlockPlacer blockPlacer;
    LinkedList<BlockPos> queue;

    int remainingDelay;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()){
            onDisable();
            return;
        }
        remainingDelay = placeDelay.getValue();
        queue = new LinkedList<>();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        queue = null;
        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);
    }

    @Subscribe
    public Listener<Render3dE> renderQueue = new Listener<>(render3dE -> {
        for(BlockPos pos : queue){
            new BlockRenderBuilder(pos)
                    .addRenderModes(
                            new RenderMode(RenderMode.renderModes.Fill, color.getValue().toRGB())
                    ).render();
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> motionEvent = new Listener<>(event -> {
       if(fullNullCheck()) return;

       //Checks if u have needed blocks in the hotbar
        if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(getBlock())) == -1){
            MessageUtil.sendError("No " + mode.getValue() + " found in the hotbar | Disabling", -1117);
            onDisable();
            return;
        }

        //Refilling queue
        boolean Single = logicMode.getValue(logic.Single) || logicMode.getValue(logic.Both);
        boolean Double = logicMode.getValue(logic.Double) || logicMode.getValue(logic.Both);
        HoleUtil.calculateHoles(radius.getValue(), Single, Double).forEach(pos -> {
            if(!queue.contains(pos) && isValid(pos))
                queue.add(pos);
        });

       //Has passed delay check and resetting
       remainingDelay--;
       if(remainingDelay > 0) return;
       remainingDelay = placeDelay.getValue();

       //Sorting poses by distance to the player
       queue.sort(Comparator.comparing(pos -> mc.player.getPositionVector().distanceTo(new Vec3d(pos).add(0.5, -0.5, 0.5))));

        //Placing and removing placed blocks
        List<BlockPos> toRemove = new ArrayList<>();
        for(BlockPos pos : queue){
            if(isValid(pos)) {
                blockPlacer.placeBlock(pos, getBlock(), false);
                toRemove.add(pos);
                break;
            }else{
                toRemove.add(pos);
            }
        }

        queue.removeAll(toRemove);

        if(shouldToggle.getValue() && queue.isEmpty())
            onDisableLog();

        if(disableOnJump.getValue() && (mc.player.movementInput.jump
                || (mc.player.collidedHorizontally && Firework.moduleManager.getModuleByClass(Step.class).isEnabled.getValue())))
            onDisableLog();
    });

    private boolean isValid(BlockPos pos){
        if(mode.getValue(modes.Obby))
            return BlockUtil.isAir(pos) && BlockUtil.isValid(pos);
        else if(mode.getValue(modes.Web))
            return BlockUtil.isAir(pos);

        return false;
    }

    private Block getBlock(){
        if(mode.getValue(modes.Obby))
            return Blocks.OBSIDIAN;
        else if(mode.getValue(modes.Web))
            return Blocks.WEB;

        return null;
    }
}
