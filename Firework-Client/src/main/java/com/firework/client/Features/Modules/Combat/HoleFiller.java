package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.World.Burrow;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Events.WorldClientInitEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.HoleUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.TickTimer;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.InventoryUtil.getHotbarItemSlot;

@ModuleManifest(name = "HolleFiller", category = Module.Category.COMBAT)
public class HoleFiller extends Module {

    //Timing
    private Setting<Boolean> timingPage = new Setting<>("Timing", true, this).setMode(Setting.Mode.SUB);

    private Setting<timers> timerMode = new Setting<>("TimerMode", timers.Ms, this).setVisibility(v-> timingPage.getValue());
    private enum timers{
        Tick, Ms
    }
    private Setting<Integer> placedDelayMs = new Setting<>("PlaceDelayMs", 40, this, 0, 100).setVisibility(v-> timerMode.getValue(timers.Ms) && timingPage.getValue());
    private Setting<Integer> placedDelayTicks = new Setting<>("PlaceDelayTicks", 2, this, 0, 100).setVisibility(v-> timerMode.getValue(timers.Tick) && timingPage.getValue());

    //Interaction
    private Setting<Boolean> interactionPage = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this).setVisibility(v-> interactionPage.getValue());
    private Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interactionPage.getValue());
    private Setting<Boolean> packet = new Setting<>("Packet", false, this).setVisibility(v-> interactionPage.getValue());

    //Customization
    private Setting<Boolean> customizationPage = new Setting<>("Customization", false, this).setMode(Setting.Mode.SUB);

    private Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", true, this).setVisibility(v-> customizationPage.getValue());
    private Setting<Boolean> shouldDisableOnJump = new Setting<>("JumpDisable", true, this).setVisibility(v-> customizationPage.getValue());
    private Setting<Boolean> autoBurrow = new Setting<>("AutoBurrow", false, this).setVisibility(v-> customizationPage.getValue());


    private Setting<Integer> radius = new Setting<>("Radius", 5, this, 0, 10);
    private Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);

    private Timer placeTimerMs;
    private TickTimer placeTimerTicks;

    private BlockPlacer blockPlacer;

    private ArrayList<BlockPos> line;

    private ArrayList<BlockPos> placedBlocks;


    @Override
    public void onEnable() {
        super.onEnable();
        placeTimerMs = new Timer();
        placeTimerMs.reset();

        placeTimerTicks = new TickTimer();
        placeTimerTicks.reset();

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);

        line = new ArrayList<>();
        placedBlocks = new ArrayList<>();

        if(autoBurrow.getValue())
            Firework.moduleManager.getModuleByClass(Burrow.class).toggle();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blockPlacer = null;

        placeTimerMs.reset();
        placeTimerTicks.reset();
        placeTimerTicks.destory();
        placeTimerTicks = null;

        line = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        if (shouldDisableOnJump.getValue() && mc.gameSettings.keyBindJump.isKeyDown()) {
            onDisable();
        }

        if (mc.player.collidedHorizontally && shouldDisableOnJump.getValue()) {
            onDisable();
        }

        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            onDisable();
            return;
        }

        for(BlockPos pos : HoleUtil.calculateSingleHoles(radius.getValue(), true)){
            if(isAir(pos) && !line.contains(pos))
                line.add(pos);
        }

        placedBlocks.clear();

        for(BlockPos pos : line){
            if(!HoleUtil.isValid(pos)){
                placedBlocks.add(pos);
                continue;
            }
            if(timerMode.getValue(timers.Ms)){
                if(placeTimerMs.hasPassedMs(placedDelayMs.getValue())){
                    processBlock(pos);
                    placeTimerMs.reset();
                }else
                    break;
            }else{
                if(placeTimerTicks.hasPassedTicks(placedDelayTicks.getValue())){
                    processBlock(pos);
                    placeTimerTicks.reset();
                }else
                    break;
            }

        }

        line.removeAll(placedBlocks);

        if(shouldToggle.getValue() && HoleUtil.calculateSingleHoles(radius.getValue(), true).isEmpty())
            onDisable();
    });

    public void processBlock(BlockPos pos){
        blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
        placedBlocks.add(pos);
        placeTimerMs.reset();
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(event -> {
        if(line == null) return;
        for(BlockPos pos : line){
            new BlockRenderBuilder(pos)
                    .addRenderModes(
                            new RenderMode(RenderMode.renderModes.Fill, color.getValue().toRGB())
                    ).render();
        }
    });

    @SubscribeEvent
    public void onWorldJoin(WorldClientInitEvent event) {
        placeTimerMs = new Timer();
        placeTimerMs.reset();

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);

        line = new ArrayList<>();
    }

    @Subscribe
    public Listener<SettingChangeValueEvent> onSettingsChangeEvent = new Listener<>(event -> {
        if(event.setting == timerMode){
            if(timerMode.getValue(timers.Ms)){
                placeTimerMs.reset();
            }else if(timerMode.getValue(timers.Tick)){
                placeTimerTicks.reset();
            }
        }
    });

    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }
}
